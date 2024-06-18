package LAS.DataSourceV1

import com.github.mreutegg.laszip4j.laslib.LASwriterLAS
import com.github.mreutegg.laszip4j.laszip.LASpoint
import org.apache.hadoop.fs.Path
import org.apache.hadoop.mapreduce.TaskAttemptContext
import org.apache.spark.internal.Logging
import org.apache.spark.sql.catalyst.InternalRow
import org.apache.spark.sql.execution.datasources.{CodecStreams, OutputWriter}
import org.apache.spark.sql.types.StructType

import java.io.PrintStream

/** @param path
  * @param dataSchema
  * @param context
  * @param params
  */
class LASOutputWriter(
    path: String,
    dataSchema: StructType,
    context: TaskAttemptContext,
    params: String
) extends OutputWriter
    with Logging {

  private val stream =
    new PrintStream(CodecStreams.createOutputStream(context, new Path(path)))

  //private val las_header = new LASheader()
  private val las_writer = new LASwriterLAS()
  //las_writer.open(stream, las_header, 'a', 4, 10)

  override def write(row: InternalRow): Unit = {
    val typed_row = row.toSeq(dataSchema)

    val point = new LASpoint()
    //point.set_x(typed_row(0))
    //point.set_y(typed_row(1))
    //point.set_z(typed_row(3))

    las_writer.write_point(point)
  }
  override def close(): Unit = {
    //las_writer.update_header(las_header)
    las_writer.close()
  }

  override def path(): String = path
}
