package LAS

import org.apache.spark.sql.connector.write.{Write, WriteBuilder}

/**
  */
class LASWriteBuilder extends WriteBuilder {
  override def build(): Write = super.build()

}
