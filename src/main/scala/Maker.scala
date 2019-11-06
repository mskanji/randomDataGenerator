import org.scalacheck.Gen
import scala.reflect.api
import scala.reflect.api.{TypeCreator, Universe}
import scala.reflect.runtime.universe._

/**
 * Case Class Genrator
 * params @T: TypeTag (case class)
 * Output: Map[String,Any]
 */


object Maker {


  val mirror = runtimeMirror(getClass.getClassLoader)

  var makerRunNumber = 0

  def apply[T: TypeTag]: T = {
    val method = typeOf[T].companion.decl(TermName("apply")).asMethod
    val params = method.paramLists.head
    val args = params.map { param =>
      makerRunNumber += 1
      param.info match {
        case t if t <:< typeOf[Enumeration#Value] => chooseEnumValue(convert(t).asInstanceOf[TypeTag[_ <: Enumeration]])
        case t if t =:= typeOf[Int] => makerRunNumber
        case t if t =:= typeOf[Long] => makerRunNumber
        case t if t =:= typeOf[StringItem] => Gen.listOfN(5, Gen.alphaChar).map(_.mkString).sample
        case t if t =:= typeOf[IntegerItem] => Gen.choose(1, 1000L).sample
        case t if t <:< typeOf[Option[_]] => None // specify option field here
        case t if t =:= typeOf[String] => s"id-$makerRunNumber"
        case t if t =:= typeOf[Boolean] => false  //TODO 0 ou 1 Int
        case t if t <:< typeOf[Seq[_]] => List.empty
        case t if t <:< typeOf[Map[_, _]] => Map.empty
        case t if isCaseClass(t) => apply(convert(t))
        case t => throw new Exception(s"Maker doesn't support generating $t")
      }
    }

    val obj = mirror.reflectModule(typeOf[T].typeSymbol.companion.asModule).instance
    mirror.reflect(obj).reflectMethod(method)(args: _*).asInstanceOf[T]
  }

  def chooseEnumValue[E <: Enumeration : TypeTag]: E#Value = {
    val parentType = typeOf[E].asInstanceOf[TypeRef].pre
    val valuesMethod = parentType.baseType(typeOf[Enumeration].typeSymbol).decl(TermName("values")).asMethod
    val obj = mirror.reflectModule(parentType.termSymbol.asModule).instance

    mirror.reflect(obj).reflectMethod(valuesMethod)().asInstanceOf[E#ValueSet].head
  }

  def convert(tpe: Type): TypeTag[_] = {
    TypeTag.apply(
      runtimeMirror(getClass.getClassLoader),
      new TypeCreator {
        override def apply[U <: Universe with Singleton](m: api.Mirror[U]) = {
          tpe.asInstanceOf[U#Type]
        }
      }
    )
  }

  def isCaseClass(t: Type) = {
    t.companion.decls.exists(_.name.decodedName.toString == "apply") &&
      t.decls.exists(_.name.decodedName.toString == "overrideCopy")
  }


}
