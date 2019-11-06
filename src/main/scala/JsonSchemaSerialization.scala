import org.json4s._
import org.json4s.jackson.Serialization

/**
 * Serialize Json schema to Map [String,PropertyItem]
 * Implicit Params: @formats:  Formats
 * Output: Map [String,PropertyItem]
 **/


object JsonSchemaSerialization {

  class RootSerializer extends Serializer[JsonSchemaClass] {
    private val RootClass = classOf[JsonSchemaClass]
    implicit val formats = DefaultFormats

    def deserialize(implicit format: Formats): PartialFunction[(TypeInfo, JValue), JsonSchemaClass] = {
      case (TypeInfo(RootClass, _), json) =>
        json match {
          case JObject(_) =>
            val id = (json \ "id").extractOpt[String]
            val title = (json \ "title").extractOpt[String]
            val description = (json \ "description").extractOpt[String]
            val $schema = (json \ "$schema").extractOpt[String]
            val self = (json \ "self").extractOpt[Self]
            val $metadata = (json \ "$metadata").extractOpt[Metadata]
            val required = (json \ "required").extract[List[String]]
            val typ = (json \ "type").extractOpt[String]

            implicit val fo: Formats = Serialization.formats(PropertyItemHints)

            val properties = extractWithHint(json \ "properties")
            JsonSchemaClass(
              typ.getOrElse(ItemTypes.ObjectType),
              $schema,
              self,
              $metadata,
              id,
              title,
              description,
              properties,
              required
            )
          case x => throw new MappingException("Invalid schema")
        }
    }

    def serialize(implicit format: Formats): PartialFunction[Any, JValue] = Map()
  }

  def extractWithHint(jvalue: JValue): Map[String, PropertyItem] = {
    implicit val formats: Formats = Serialization.formats(PropertyItemHints).withTypeHintFieldName("type")
    jvalue.extract[Map[String, PropertyItem]]

  }

  object PropertyItemHints extends TypeHints {

    // map class to type and viceversa
    val classToHint: Map[Class[_], String] = Map(
      classOf[StringItem] -> ItemTypes.StringType,
      classOf[BooleanItem] -> ItemTypes.BooleanType,
      classOf[NumberItem] -> ItemTypes.NumberType,
      classOf[IntegerItem] -> ItemTypes.IntegerType,
      classOf[ObjectItem] -> ItemTypes.ObjectType,
      classOf[ArrayItem] -> ItemTypes.ArrayType,
      classOf[RefItem] -> ItemTypes.RefType,
      classOf[PatchItem] -> ItemTypes.PatchType
    )
    val hintToClass: Map[String, Class[_]] = classToHint.map(_.swap)

    override val hints: List[Class[_]] =
      List(
        classOf[StringItem],
        classOf[BooleanItem],
        classOf[NumberItem],
        classOf[IntegerItem],
        classOf[ObjectItem],
        classOf[ArrayItem],
        classOf[RefItem],
        classOf[PatchItem]
      )

    override def classFor(hint: String): Option[Class[_]] = hintToClass.get(hint) match {
      case Some(c) => Some(c)
      case None => throw new MappingException("Invalid Type Exception")
    }

    override def hintFor(clazz: Class[_]): String = classToHint(clazz)
  }

  implicit val formats: Formats = Serialization
    .formats(PropertyItemHints)
    .withTypeHintFieldName("type") + new RootSerializer()
}





