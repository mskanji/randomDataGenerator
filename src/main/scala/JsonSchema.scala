trait JsonSchema {

  val `type`: String
  val description: Option[String]

}


case class JsonSchemaClass(
                            `type`: String = "object",
                            $schema: Option[String] = None,
                            self: Option[Self] = None,
                            $metadata: Option[Metadata] = None,
                            id: Option[String] = None,
                            title: Option[String] = None,
                            description: Option[String] = None,
                            properties: Map[String, PropertyItem],
                            required: List[String] = Nil

                          ) extends JsonSchema

case class Self(
                 vendor: Option[String] = None,
                 name: Option[String] = None,
                 format: Option[String] = None,
                 version: Option[String] = None
               )

case class Metadata(
                     $datasource: Option[String] = None,
                     $dataset: Option[String] = None,
                     $fileFormat: Option[String] = None,
                     $nameFormat: Option[String] = None,
                     $separator: Option[String] = None,
                     $quote: Option[String] = None,
                     $escape: Option[String] = None,
                     $ingestionMode: Option[String] = None,
                     $dataVector: Option[String] = None,
                     $dateFormat: Option[String] = None,
                     $dateTimeFormat: Option[String] = None
                   )


trait PropertyItem extends JsonSchema

case class ObjectItem(
                       title: Option[String] = None,
                       description: Option[String] = None,
                       properties: Map[String, PropertyItem],
                       required: List[String] = Nil,
                       $metadata: Option[PropertyMetadata] = None
                     ) extends PropertyItem {
  override val `type`: String = ItemTypes.ObjectType
}

case class ArrayItem(
                      description: Option[String] = None,
                      $metadata: Option[PropertyMetadata] = None,
                      items: PropertyItem
                    ) extends PropertyItem {
  override val `type`: String = ItemTypes.ArrayType
}

trait ValuePropertyItem extends PropertyItem

final case class StringItem(
                             description: Option[String] = None,
                             $metadata: Option[PropertyMetadata] = None,
                             pattern: Option[String] = None,
                             format: Option[String] = None,
                             $default: Option[String] = None,
                             enum: List[String] = Nil

                           ) extends ValuePropertyItem {
  override val `type`: String = ItemTypes.StringType
}

case class BooleanItem(
                        description: Option[String] = None,
                        $metadata: Option[PropertyMetadata] = None,
                        $default: Option[Boolean] = None,
                        enum: List[String] = Nil
                      ) extends ValuePropertyItem {
  override val `type`: String = ItemTypes.BooleanType
}

final case class NumberItem(
                             description: Option[String] = None,
                             $metadata: Option[PropertyMetadata] = None,
                             $default: Option[Double] = None,
                             format: Option[String] = None
                           ) extends ValuePropertyItem {
  override val `type`: String = ItemTypes.NumberType
}

final case class IntegerItem(
                              description: Option[String] = None,
                              $metadata: Option[PropertyMetadata] = None,
                              $default: Option[Long] = None,
                              format: Option[String] = None
                            ) extends ValuePropertyItem {
  override val `type`: String = ItemTypes.IntegerType
}

final case class RefItem(
                          description: Option[String] = None,
                          $metadata: Option[PropertyMetadata] = None,
                          $ref: Option[String] = None,
                          $default: Option[String] = None
                        ) extends ValuePropertyItem {
  override val `type`: String = ItemTypes.RefType
}

final case class PatchItem(
                            description: Option[String] = None,
                            $metadata: Option[PropertyMetadata] = None
                          ) extends ValuePropertyItem {
  override val `type`: String = ItemTypes.PatchType
}

final case class PropertyMetadata(
                                   $privacyLevel: Option[String] = None,
                                   $alias: Option[String] = None,
                                   $dataKey: Option[String] = None,
                                   $PropertyMetadata: Option[String] = None,
                                   $dataValidationMandatory: Option[Boolean] = None,
                                   $dataValidationRules: Option[String] = None,
                                   FALLBACK_NAME: Option[String] = None,
                                   $tags: List[(String, String)] = Nil,
                                   PATCH_TARGET_SCHEMA: Option[String] = None,
                                   PATCH_RESOURCE_ID: Option[String] = None
                                 )


object ItemTypes {
  val StringType = "string"
  val NumberType = "number"
  val IntegerType = "integer"
  val BooleanType = "boolean"
  val ObjectType = "object"
  val ArrayType = "array"
  val RefType = "ref"
  val PatchType = "patch"
}
