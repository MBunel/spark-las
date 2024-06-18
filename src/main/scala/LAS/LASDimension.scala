package LAS

import org.apache.spark.sql.types._

/** List of LAS Dimensions
  */
abstract class LASDimension extends Serializable {

}

object LASDimension extends Enumeration {
  type LASDimension = StructField
  // Transform in enum ?
  private val X = StructField("X", FloatType, nullable = false)
  private val Y = StructField("Y", FloatType, nullable = false)
  private val Z = StructField("Z", FloatType, nullable = false)
  private val INTENSITY = StructField("Intensity", ShortType, nullable = true)
  private val RETURN_NUMBER =
    StructField("Return number", ByteType, nullable = false)
  private val NUMBER_OF_RETURNS =
    StructField("Number of returns", ByteType, nullable = false)
  private val SCAN_DIRECTION_FLAG =
    StructField("Scan direction flag", ByteType, nullable = false)
  private val EDGE_OF_FLIGHT_LINE =
    StructField("Edge of flight line", ByteType, nullable = false)
  private val CLASSIFICATION =
    StructField("Classification", ShortType, nullable = false)
  private val SCAN_ANGLE_RANK =
    StructField("Scan angle rank", ShortType, nullable = false)
  private val USER_DATA = StructField("User data", ShortType, nullable = true)
  private val POINTG_SOURCE_ID =
    StructField("Point source ID", IntegerType, nullable = false)
  private val GPS_TIME =
    StructField("GPS Time", DoubleType, nullable = false)
  private val RED = StructField("Red", ShortType, nullable = false)
  private val GREEN = StructField("Green", ShortType, nullable = false)
  private val BLUE = StructField("Blue", ShortType, nullable = false)
  private val WAVE_PACKET_DESCRIPTIOR_INDEX = StructField(
    "Wave Packet Descriptor Index",
    ShortType,
    nullable = false
  )
  private val BYTE_OFFSET_TO_WAVEFORM_DATA = StructField(
    "Byte Offset to Waveform Data",
    ShortType,
    nullable = false
  )
  private val WAVEFORM_PACKET_SIZE_IN_BYTES = StructField(
    "Waveform Packet Size in Byte",
    ShortType,
    nullable = false
  )
  private val RETURN_POINT_WAVEFORM_LOCATION = StructField(
    "Return Point Waveform Location",
    FloatType,
    nullable = false
  )
  private val PARAMETRIC_DX =
    StructField("Parametric dx", FloatType, nullable = false)
  private val PARAMETRIC_DY =
    StructField("Parametric dy", FloatType, nullable = false)
  private val PARAMETRIC_DZ =
    StructField("Parametric dz", FloatType, nullable = false)
  private val SYNTHETIC_FLAG =
    StructField("Is Synthetic", BooleanType, nullable = true)
  private val KEY_POINT_FLAG =
    StructField("Is key-point", BooleanType, nullable = true)
  private val WITHHELD_FLAG =
    StructField("Is Withheld", BooleanType, nullable = true)
  private val OVERLAP_FLAG =
    StructField("Is Overlap", BooleanType, nullable = true)
  private val SCANNER_CHANNEL =
    StructField("Scanner Channel", ShortType, nullable = false)
  private val NIR = StructField("NIR", ShortType, nullable = false)

  private val GEOM = Array(X, Y, Z)
  private val RVB = Array(RED, GREEN, BLUE)
  private val WAVE_PACKETS = Array(
    WAVE_PACKET_DESCRIPTIOR_INDEX,
    BYTE_OFFSET_TO_WAVEFORM_DATA,
    WAVEFORM_PACKET_SIZE_IN_BYTES,
    RETURN_POINT_WAVEFORM_LOCATION,
    PARAMETRIC_DX,
    PARAMETRIC_DY,
    PARAMETRIC_DZ
  )
  private val CLASSIFICATION_FLAGS =
    Array(SYNTHETIC_FLAG, KEY_POINT_FLAG, WITHHELD_FLAG, OVERLAP_FLAG)

  final def pointFormatToSchema(dataRecordFormat: Int): Array[StructField] = {
    dataRecordFormat match {
      case 0 =>
        GEOM ++ Array(
          INTENSITY,
          RETURN_NUMBER,
          NUMBER_OF_RETURNS,
          SCAN_DIRECTION_FLAG,
          EDGE_OF_FLIGHT_LINE,
          CLASSIFICATION,
          SCAN_ANGLE_RANK,
          USER_DATA,
          POINTG_SOURCE_ID
        )
      case 1 =>
        pointFormatToSchema(0) :+ GPS_TIME
      case 2 => pointFormatToSchema(0) ++ RVB
      case 3 => pointFormatToSchema(1) ++ RVB
      case 4 => pointFormatToSchema(1) ++ WAVE_PACKETS
      case 5 => pointFormatToSchema(3) ++ WAVE_PACKETS
      case 6 =>
        GEOM ++ Array(INTENSITY, RETURN_NUMBER, NUMBER_OF_RETURNS) ++
          CLASSIFICATION_FLAGS ++
          Array(
            SCANNER_CHANNEL,
            SCAN_DIRECTION_FLAG,
            EDGE_OF_FLIGHT_LINE,
            CLASSIFICATION,
            USER_DATA,
            SCAN_ANGLE_RANK,
            POINTG_SOURCE_ID,
            GPS_TIME
          )
      case 7  => pointFormatToSchema(6) ++ RVB
      case 8  => pointFormatToSchema(7) :+ NIR
      case 9  => pointFormatToSchema(6) ++ WAVE_PACKETS
      case 10 => pointFormatToSchema(9) ++ RVB :+ NIR
      case _  => null
    }

  }

  final def dimensionsToSchema(dimension: Seq[String]): Array[StructType] = ???
  // TODO Find a way to iterate on values for transform a list of dims in Array of struct

}
