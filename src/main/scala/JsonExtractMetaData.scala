import java.io.{File, FileInputStream, InputStream}
import com.fasterxml.jackson.databind.{JsonNode, ObjectMapper}
import org.json4s.JValue
import org.json4s.jackson.JsonMethods.fromJsonNode

/**
 * Json-schema should be valid
 * extract Hints to case class
 * params: @file : File
 * Output: JsonSchemaClass
 **/

object JsonExtractMetaData {

  def extractJsonSchemaClass(file: File): JsonSchemaClass = {
    deserialiseJsonSchemaClass(validateJSONSyntax(new FileInputStream(file)))
  }

  def deserialiseJsonSchemaClass(jsonNode: JsonNode): JsonSchemaClass = {
    val jValue: JValue = fromJsonNode(jsonNode)
    import JsonSchemaSerialization.formats
    jValue.extract[JsonSchemaClass]
  }


  def validateJSONSyntax(jsonInputStream: InputStream): JsonNode = {

    val mapper: ObjectMapper = new ObjectMapper()
    val jsonNode = mapper.readTree(jsonInputStream)
    jsonNode

  }


}