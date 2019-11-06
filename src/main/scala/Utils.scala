import java.io.FileInputStream
import java.util.{Date, Properties}
import org.scalacheck.Gen

object Utils {

  def typeItemOf[T](v: T) = v match {
    case _: IntegerItem => Gen.choose(1, 1000L).sample
    case _: StringItem => Gen.listOfN(35, Gen.alphaChar).map(_.mkString).sample
    case _: NumberItem => Gen.choose(10000, 100000L).sample
  }

  def mkdirDirectory(o: String): Unit = {
    import java.nio.file.{Path, Paths}
    val folderPath: Path = Paths.get(getProp(o))

  }

  def getProp(propertyName: String): String = {

    val properties = new Properties()
    properties.load(new FileInputStream("Config/ressources.properties"))
    val property = properties.getProperty(propertyName)
    property.toString

  }


  def Generator(m: (Boolean, String)) = {
    m match {
      case (true, "string") => new Date(Gen.choose(100000000, 10000000000L).sample.get)
      case (false, "string") => Gen.listOfN(35, Gen.alphaChar).map(_.mkString).sample.get
      case (false, "integer") => Gen.choose(10, 100L).sample.get
      //TODO ADD SOME  HERE TO COVER ALL USES CASES IN SCHEMA ....
    }
  }


}
