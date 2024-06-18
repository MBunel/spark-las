package LAS.DataSourceV1

import org.apache.hadoop.fs.FileStatus
import org.apache.hadoop.mapreduce.{Job, TaskAttemptContext}
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.execution.datasources.{CodecStreams, OutputWriter, OutputWriterFactory, TextBasedFileFormat}
import org.apache.spark.sql.sources.DataSourceRegister
import org.apache.spark.sql.types.StructType

/** LAS reader / writer using DataSource V1 API (fallback Datasource V2)
  */
class LASFileFormat extends TextBasedFileFormat with DataSourceRegister {
  override def shortName(): String = "las"

  override def inferSchema(
      sparkSession: SparkSession,
      options: Map[String, String],
      files: Seq[FileStatus]
  ): Option[StructType] = ???

  override def prepareWrite(
      sparkSession: SparkSession,
      job: Job,
      options: Map[String, String],
      dataSchema: StructType
  ): OutputWriterFactory = {
    val conf = job.getConfiguration

    new OutputWriterFactory {
      override def newInstance(
          path: String,
          dataSchema: StructType,
          context: TaskAttemptContext
      ): OutputWriter = {
        new LASOutputWriter(path, dataSchema, context, "")
      }

      override def getFileExtension(context: TaskAttemptContext): String = {
        ".las" + CodecStreams.getCompressionExtension(context)
      }
    }
  }
}
