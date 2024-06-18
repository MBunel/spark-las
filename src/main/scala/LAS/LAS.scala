package LAS

import LAS.DataSourceV1.LASFileFormat
import org.apache.spark.sql.execution.datasources.FileFormat
import org.apache.spark.sql.execution.datasources.v2.FileDataSourceV2
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.util.CaseInsensitiveStringMap

/** Class Las
  *
  * Main class for the las datasource.
  *
  * Inspired by the spark csv and parquet readers
  *
  * TODO: Schema inference; write las; filters
  */
class LAS extends FileDataSourceV2 {

  /** Get Table with a user schema
    *
    * @param options
    * @param schema
    * @return
    */
  override def getTable(
      options: CaseInsensitiveStringMap,
      schema: StructType
  ): LASTable = {
    val paths = getPaths(options)
    val tableName = getTableName(options, paths)
    val optionsWithoutPaths = getOptionsWithoutPaths(options)
    LASTable(
      tableName,
      sparkSession,
      optionsWithoutPaths,
      paths,
      Some(schema),
      fallbackFileFormat
    )
  }

  /** @return */
  override def shortName(): String = "las"

  /** @return */
  override def supportsExternalMetadata(): Boolean = true

  /** @param options
    * @return
    */
  override protected def getTable(
      options: CaseInsensitiveStringMap
  ): LASTable = {
    val paths = getPaths(options)
    val tableName = getTableName(options, paths)
    val optionsWithoutPaths = getOptionsWithoutPaths(options)
    LASTable(
      tableName,
      sparkSession,
      optionsWithoutPaths,
      paths,
      None,
      fallbackFileFormat
    )
  }

  /** @return */
  override def fallbackFileFormat: Class[_ <: FileFormat] =
    classOf[LASFileFormat]
}
