package LAS

import org.apache.spark.broadcast.Broadcast
import org.apache.spark.sql.catalyst.InternalRow
import org.apache.spark.sql.connector.read.PartitionReader
import org.apache.spark.sql.execution.datasources.PartitionedFile
import org.apache.spark.sql.execution.datasources.v2.FilePartitionReaderFactory
import org.apache.spark.sql.types.StructType
import org.apache.spark.util.SerializableConfiguration

/** A Factory used to create Las Readers
  */
case class LASPartitionReaderFactory(
    broadcastedConf: Broadcast[SerializableConfiguration],
    readDataSchema: StructType,
    options: LASOptions
) extends FilePartitionReaderFactory {

  override def buildReader(
      partitionedFile: PartitionedFile
  ): PartitionReader[InternalRow] = {
    val conf = broadcastedConf.value.value
    val path = partitionedFile.pathUri

    // We return a LasPartitionReader according to the "las_reader" parameter
    options.las_reader match {
      case "las4j" => new LAS4jReader(conf, readDataSchema, path)
      // Commented for remove all Pdal deps
      // case "pdal"  => new LASPdalReader(conf, readDataSchema, path)
    }
  }
}
