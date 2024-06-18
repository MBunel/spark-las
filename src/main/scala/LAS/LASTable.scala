package LAS

import org.apache.hadoop.fs.FileStatus
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.connector.write.{LogicalWriteInfo, WriteBuilder}
import org.apache.spark.sql.execution.datasources.FileFormat
import org.apache.spark.sql.execution.datasources.v2.{FileTable, FileWrite}
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.util.CaseInsensitiveStringMap

/** @param name
  * @param sparkSession
  * @param options
  * @param paths
  * @param userSpecifiedSchema
  * @param fallbackFileFormat
  */
case class LASTable(
    name: String,
    sparkSession: SparkSession,
    options: CaseInsensitiveStringMap,
    paths: Seq[String],
    userSpecifiedSchema: Option[StructType],
    fallbackFileFormat: Class[_ <: FileFormat]
) extends FileTable(sparkSession, options, paths, userSpecifiedSchema) {

  override def inferSchema(files: Seq[FileStatus]): Option[StructType] = {
    //val parsedOptions = new LASOptions(options)
    Las4JDataSource.inferSchema(sparkSession, files, null)
    //PdalDataSource.inferSchema(sparkSession, files, null)
  }

  override def formatName: String = "LAS"

  override def newScanBuilder(
      options: CaseInsensitiveStringMap
  ): LASScanBuilder =
    new LASScanBuilder(sparkSession, fileIndex, schema, dataSchema, options)

  //override def supportsDataType(dataType: DataType): Boolean = super.supportsDataType(dataType)

  override def newWriteBuilder(info: LogicalWriteInfo): WriteBuilder =
    new WriteBuilder {
      override def build(): FileWrite =
        LASWrite(paths, formatName, supportsDataType, info)
    }
}
