package LAS

import org.apache.hadoop.mapreduce.{Job, TaskAttemptContext}
import org.apache.spark.sql.connector.write.LogicalWriteInfo
import org.apache.spark.sql.execution.datasources.v2.FileWrite
import org.apache.spark.sql.execution.datasources.{OutputWriter, OutputWriterFactory}
import org.apache.spark.sql.internal.SQLConf
import org.apache.spark.sql.types.{DataType, StructType}

case class LASWrite(
    paths: Seq[String],
    formatName: String,
    supportsDataType: DataType => Boolean,
    info: LogicalWriteInfo
) extends FileWrite {

  override def prepareWrite(
      sqlConf: SQLConf,
      job: Job,
      options: Map[String, String],
      dataSchema: StructType
  ): OutputWriterFactory = {

    // Options for writer
    val LasOptions = new LASOptions(options)

    new OutputWriterFactory {
      override def getFileExtension(context: TaskAttemptContext): String =
        ".las"

      override def newInstance(
          path: String,
          dataSchema: StructType,
          context: TaskAttemptContext
      ): OutputWriter = LASOutputWriter(path, dataSchema, context, LasOptions)
    }
  }
}
