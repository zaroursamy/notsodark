package order

import org.apache.spark.sql.{Dataset, SparkSession}

class OrderCreateDTReader(sparkSession: SparkSession) {

  import sparkSession.implicits._

  def readParquet: Dataset[OrderCreateDT] = sparkSession
    .read
    .format("parquet")
    .load("create-order")
    .as[OrderCreateDT]
    .cache
}
