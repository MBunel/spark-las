package LAS

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.functions.col
import org.apache.spark.sql.types._
import org.apache.spark.sql.{DataFrame, DataFrameReader, SparkSession}
import org.scalatest.BeforeAndAfterAll
import org.scalatest.funsuite.FunSuiteLike

class LASTest extends FunSuiteLike with BeforeAndAfterAll {

  // Set to Level.WARN is you want verbosity
  Logger.getLogger("org").setLevel(Level.OFF)
  Logger.getLogger("akka").setLevel(Level.OFF)

  private val master = "local[2]"
  private val appName = "spark-lasTest"

  private var spark: SparkSession = _

  override protected def beforeAll(): Unit = {
    super.beforeAll()
    spark = SparkSession
      .builder()
      .master(master)
      .appName(appName)
      .getOrCreate()
  }

  override protected def afterAll(): Unit = {
    try {
      spark.sparkContext.stop()
    } finally {
      super.afterAll()
    }
  }

  test("Readlas test: Do you send back a DataFrameReader?") {
    val results = spark.read.format("LAS.LAS")
    assert(results.isInstanceOf[DataFrameReader])
  }

  // Test DataFrame
  test("DataFrame test: can you really make a DF from the data") {
    val results = spark.read
      .format("LAS.LAS")
      .load("src/test/resources/test_data_1.laz")
    assert(results.isInstanceOf[DataFrame])
  }

  test("Reading test: Las 1.1") {
    val results = spark.read
      .format("LAS.LAS")
      .load("src/test/resources/test_data_4.laz")
    assert(results.select(col("Point source ID")).count().toInt == 17607)
  }

  test("Reading test: Las 1.2") {
    val results = spark.read
      .format("LAS.LAS")
      .load("src/test/resources/test_data_1.laz")
    assert(results.select(col("Point source ID")).count().toInt == 17607)
  }

  test("Reading test: Las 1.3") {
    val results = spark.read
      .format("LAS.LAS")
      .load("src/test/resources/test_data_5.laz")
    assert(results.select(col("Point source ID")).count().toInt == 17607)
  }

  test("Reading test: Las 1.4") {
    val results = spark.read
      .format("LAS.LAS")
      .load("src/test/resources/test_data_6.laz")
    assert(results.select(col("Point source ID")).count().toInt == 17607)
  }

  test("Data distribution test: Can you count all elements?") {
    val results = spark.read
      .format("LAS.LAS")
      .load("src/test/resources/test_data_1.laz")
    assert(results.select(col("Point source ID")).count().toInt == 17607)
  }

  test("Multi-files reading (glob)") {
    val results = spark.read
      .format("LAS.LAS")
      .load("src/test/resources/*.laz")
    assert(results.select(col("Point source ID")).count().toInt == 102375)
    assert(results.isInstanceOf[DataFrame])
  }

  test("Multi-files reading (comma)") {
    val results = spark.read
      .format("LAS.LAS")
      .load(
        "src/test/resources/test_data_1.laz",
        "src/test/resources/test_data_2.laz",
        "src/test/resources/test_data_3.laz"
      )
    assert(results.select(col("Point source ID")).count().toInt == 49554)
    assert(results.isInstanceOf[DataFrame])
  }

  test("Multi-files schema intersection") {
    val reference_schema = StructType(
      List(
        StructField("Scan direction flag", ByteType, nullable = true),
        StructField("Y", FloatType, nullable = true),
        StructField("Z", FloatType, nullable = true),
        StructField("Scan angle rank", ShortType, nullable = true),
        StructField("User data", ShortType, nullable = true),
        StructField("Point source ID", IntegerType, nullable = true),
        StructField("X", FloatType, nullable = true),
        StructField("Number of returns", ByteType, nullable = true),
        StructField("Edge of flight line", ByteType, nullable = true),
        StructField("Intensity", ShortType, nullable = true),
        StructField("Return number", ByteType, nullable = true),
        StructField("Classification", ShortType, nullable = true)
      )
    )

    val results = spark.read
      .format("LAS.LAS")
      .option("mergeSchema", value = true)
      .option("mergeSchemaMode", "intersection")
      .load("src/test/resources/*.laz")

    assert(results.schema == reference_schema)
  }
  test("Multi-files schema union") {
    val reference_schema = StructType(
      List(
        StructField("Scan direction flag", ByteType, nullable = true),
        StructField("Y", FloatType, nullable = true),
        StructField("Z", FloatType, nullable = true),
        StructField("Scan angle rank", ShortType, nullable = true),
        StructField("User data", ShortType, nullable = true),
        StructField("Point source ID", IntegerType, nullable = true),
        StructField("X", FloatType, nullable = true),
        StructField("Edge of flight line", ByteType, nullable = true),
        StructField("Intensity", ShortType, nullable = true),
        StructField("Return number", ByteType, nullable = true),
        StructField("Classification", ShortType, nullable = true),
        StructField("Number of returns", ByteType, nullable = true),
        StructField("GPS Time", DoubleType, nullable = true)
      )
    )

    val results = spark.read
      .format("LAS.LAS")
      .option("mergeSchema", value = true)
      .option("mergeSchemaMode", "union")
      .load("src/test/resources/test_data_3.laz")

    results.printSchema()

    assert(results.schema == reference_schema)
  }

}
