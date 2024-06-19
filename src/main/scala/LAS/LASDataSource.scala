package LAS

// import io.pdal.pipeline.ReadLas
import com.github.mreutegg.laszip4j.LASReader
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.Path.getPathWithoutSchemeAndAuthority
import org.apache.hadoop.fs.{FileStatus, FileSystem}
import org.apache.spark.internal.Logging
import org.apache.spark.sql.catalyst.InternalRow
import org.apache.spark.sql.types._
import org.apache.spark.sql.{Dataset, SparkSession}

/** Common functions to treat las data file
  */
abstract class LASDataSource extends Serializable {
  def inferSchema(
      sparkSession: SparkSession,
      inputPaths: Seq[FileStatus],
      parsedOptions: LASOptions
  ): Option[StructType] = {
    if (inputPaths.nonEmpty) {
      Some(infer(sparkSession, inputPaths, parsedOptions))
    } else {
      None
    }
  }

  def fieldExtractor(structField: StructField): StructField => DataType

  def readFile(): Iterator[InternalRow] = ???

  protected def infer(
      sparkSession: SparkSession,
      inputPaths: Seq[FileStatus],
      parsedOptions: LASOptions
  ): StructType

  protected final def mergeSchemas(
      schemas: Seq[StructType],
      mergeMode: String = "intersection"
  ): StructType = {
    val schemas_set = schemas.map(x => x.toSet)

    // TODO: Correct the order problem
    val mergedSchemas = mergeMode match {
      case "union"        => schemas_set.reduce((x, y) => x union y)
      case "intersection" => schemas_set.reduce((x, y) => x intersect y)
    }

    // TODO: Find a way to sort
    StructType(mergedSchemas.toArray)
  }

}

object LASDataSource extends Logging {
  def apply(options: LASOptions): LASDataSource = {
    options.las_reader match {
      case "las4j" => Las4JDataSource
      // Remove all pdal calls
      // case "pdal"  => PdalDataSource
    }
  }
}

object Las4JDataSource extends LASDataSource {
  override def fieldExtractor(
      structField: StructField
  ): StructField => DataType = ???

  def createBaseDataset(
      sparkSession: SparkSession,
      inputPaths: Seq[FileStatus],
      parsedOptions: LASOptions
  ): Dataset[String] = ???

  override protected def infer(
      sparkSession: SparkSession,
      inputPaths: Seq[FileStatus],
      parsedOptions: LASOptions
  ): StructType = {

    // TODO: Use spark structures for merge schemas
    //val headers: Dataset[String] = createBaseDataset(sparkSession, inputPaths, parsedOptions)

    // This fun made a seq of las headers
    val test = inputPaths
      .map(_.getPath)
      .map(getPathWithoutSchemeAndAuthority(_).toString)

    // Test code generate header for each file

    // find other way
    val configuration = new Configuration();
    configuration.set("fs.defaultFS", "hdfs://127.0.0.1:9000/");
    val fs = FileSystem.getLocal(configuration)

    val schemas = inputPaths
      .map(_.getPath)
      .map(x => fs.open(x))
      .map(x => LASReader.getHeader(x))
      .map(x => x.getPointDataRecordFormat)
      .map(x => StructType(LASDimension.pointFormatToSchema(x)))

    mergeSchemas(schemas)
  }
}

// Removing all pdal imports
/*
object PdalDataSource extends LASDataSource {
  override def fieldExtractor(
      structField: StructField
  ): StructField => DataType = ???

  override protected def infer(
      sparkSession: SparkSession,
      inputPaths: Seq[FileStatus],
      parsedOptions: LASOptions
  ): StructType = {

    // find other way
    val configuration = new Configuration();
    configuration.set("fs.defaultFS", "hdfs://127.0.0.1:9000/");
    val fs = FileSystem.getLocal(configuration)

    // Import implicit for dataset creation
    import sparkSession.implicits._

    // TODO Find a way to get file info on schema_dataset
    val schema_dataset = sparkSession.createDataset(
      inputPaths.map(file =>
        ReadLas(file.getPath.toUri.getPath).toPipeline.getQuickInfo()
      )
    )
    val schema_json = sparkSession.read.json(schema_dataset).toDF("header")

    val dimensions =
      schema_json.select(
        functions.split(col("header.dimensions"), ",").as("dimensions")
      )

    //val schema = LASDimension.dimensionsToSchema(dimensions.head)

    //mergeSchemas(schemas = schema)

    // Return point type schema (just for debug)
    StructType(LASDimension.pointFormatToSchema(6))
  }
}
 */
