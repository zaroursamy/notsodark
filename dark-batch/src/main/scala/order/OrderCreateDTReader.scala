package order

import org.apache.spark.sql.{Dataset, SparkSession}

class OrderCreateDTReader(sparkSession: SparkSession) {

  import sparkSession.implicits._

  def readParquet: Dataset[OrderCreateDT] = sparkSession
    .read
    .option("basePath", "create-order")
    .parquet("create-order")
    .as[OrderCreateDT]
    .cache
}
