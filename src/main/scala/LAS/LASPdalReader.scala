package LAS





// Commented for desactivate all uses of pdal

/*
import io.pdal.PointViewIterator
import io.pdal.pipeline.ReadLas
import org.apache.hadoop.conf.Configuration
import org.apache.spark.sql.catalyst.InternalRow
import org.apache.spark.sql.connector.read.PartitionReader
import org.apache.spark.sql.types.StructType

import java.net.URI

/** Las file reader, based on pdal
 * @param path
 */
class LASPdalReader(conf: Configuration, readDataSchema: StructType, path: URI)
    extends PartitionReader[InternalRow] {

  val dataSchema = readDataSchema

  private val expression = ReadLas(path.getPath)
  private val pipeline = expression.toPipeline
  pipeline.initialize()
  pipeline.execute()

  private val pvs: PointViewIterator = pipeline.getPointViews()
  private val pv = pvs.next()

  private val points_count = pv.length()
  private var counter = 0

  override def next(): Boolean = this.counter < this.points_count

  override def get(): InternalRow = {
    val out = extract_field(this.counter)
    this.counter += 1
    return out
  }

  override def close(): Unit = pvs.close()

  private def extract_field(point_index: Int): InternalRow = {
    var fields_values: Seq[Any] = Seq()

    for (field <- this.dataSchema.iterator) {

      val value = field.name match {
        case "X"                 => pv.getX(point_index).toFloat
        case "Y"                 => pv.getY(point_index).toFloat
        case "Z"                 => pv.getZ(point_index).toFloat
        case "Intensity"         => pv.getShort(point_index, "Intensity")
        case "Return number"     => pv.getShort(point_index, "ReturnNumber")
        case "Number of returns" => pv.getShort(point_index, "NumberOfReturns")
        case "Scan direction flag" =>
          pv.getShort(point_index, "ScanDirectionFlag")
        case "Edge of flight line" =>
          pv.getShort(point_index, "EdgeOfFlightLine")
        case "Classification"  => pv.getShort(point_index, "Classification")
        case "Is Synthetic"    => null
        case "Is key-point"    => null
        case "Is Withheld"     => null
        case "Is Overlap"      => null
        case "Scanner Channel" => null
        case "User data"       => null
        case "Scan angle rank" => null
        case "Point source ID" => null
        case "GPS Time"        => null
      }
      fields_values = fields_values :+ value
    }
    InternalRow.fromSeq(fields_values)
  }
}
 */
