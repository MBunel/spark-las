package LAS

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.functions.col
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

  val test_file = "src/test/resources/minilas.laz"

  test("Readlas test: Do you send back a DataFrameReader?") {
    val results = spark.read.format("LAS.LAS")
    assert(results.isInstanceOf[DataFrameReader])
  }

  // Test DataFrame
  test("DataFrame test: can you really make a DF from the data") {
    val results = spark.read
      .format("LAS.LAS")
      .load(test_file)
    assert(results.isInstanceOf[DataFrame])
  }

  test("Data distribution test: Can you count all elements?") {
    val results = spark.read
      .format("LAS.LAS")
      .load(test_file)
    assert(results.select(col("Point source ID")).count().toInt == 17607)
  }

  test("Multi-files reading (glob)") {
    val results = spark.read
      .format("LAS.LAS")
      .load("src/test/resources/*.laz")
    assert(results.select(col("Point source ID")).count().toInt == 33712)
    assert(results.isInstanceOf[DataFrame])
  }

  test("Multi-files reading (comma)") {
    val results = spark.read
      .format("LAS.LAS")
      .load(
        "src/test/resources/minilas.laz",
        "src/test/resources/minilas_2.laz"
      )
    assert(results.select(col("Point source ID")).count().toInt == 33712)
    assert(results.isInstanceOf[DataFrame])
  }

}
