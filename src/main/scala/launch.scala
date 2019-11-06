import java.io.{File}
import JsonSchemaSerialization.{PropertyItemHints, RootSerializer}
import com.danielasfregola.randomdatagenerator.RandomDataGenerator
import org.json4s._
import org.json4s.jackson.Serialization
import scala.util.{Failure, Success, Try}

object launch extends RandomDataGenerator {
  def main(args: Array[String]): Unit = {
    implicit val formats: Formats = Serialization
      .formats(PropertyItemHints)
      .withTypeHintFieldName("type") + new RootSerializer()
    val schemaFile: Try[File]  = Try(new File(Utils.getProp("JsonFileInput")))
    schemaFile match {
      case Success(source) =>
      {
        val pw = new java.io.PrintWriter(new File(Utils.getProp("Out")))
        val extractorJsonSchemaClass = (JsonExtractMetaData.extractJsonSchemaClass(source))
        val JsonSchemaClass(objectType, schema, self, metadata, id, title,  description, properties, required) = extractorJsonSchemaClass
        val  cnd =  properties.mapValues( v =>
        (v.description.get.toLowerCase.contains(Utils.getProp("DateDescInJson").toString.toLowerCase) , v.`type` ) )
        val prop = cnd.mapValues(v => Utils.Generator(v))
        var MapList = Seq[ Map[String , Any]]()
          for ( i <- 0 to Utils.getProp("itterationNumber").toInt ) {MapList = MapList :+ prop}
         val MapListValue = MapList.flatten.groupBy(_._1).mapValues(_.map(_._2))
        val keys = MapListValue.keySet.toList
        pw.println(keys.mkString(","))
        val lines = keys.map(MapListValue).transpose
        lines.foreach(line => pw.println(line.mkString(",")))
        pw.close()
      }
      case Failure(e) => println(e.getStackTrace)
    }
  }
}
