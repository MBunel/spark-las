package LAS

import com.github.mreutegg.laszip4j.laslib.LASwriterLAS
import com.github.mreutegg.laszip4j.laszip.LASpoint
import org.apache.hadoop.mapreduce.TaskAttemptContext
import org.apache.spark.internal.Logging
import org.apache.spark.sql.catalyst.InternalRow
import org.apache.spark.sql.execution.datasources.OutputWriter
import org.apache.spark.sql.types.StructType

/** Non implemented yet
  * @param path
  * @param dataSchema
  * @param context
  * @param LASOptions
  */
case class LASOutputWriter(
    path: String,
    dataSchema: StructType,
    context: TaskAttemptContext,
    LASOptions: LASOptions
) extends OutputWriter
    with Logging {

  // private val os = CodecStreams.createOutputStream(conf, new Path(path))
  // private val ps = new PrintStream(os)
  private val writer = new LASwriterLAS() //.open(ps)

  override def write(row: InternalRow): Unit = {
    // Create a point from InternalRow
    val pt = new LASpoint()

    writer.write_point(pt)
  }

  override def close(): Unit = writer.close()
}
