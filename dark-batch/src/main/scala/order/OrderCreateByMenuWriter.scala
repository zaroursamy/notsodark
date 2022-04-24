package order

import org.apache.spark.sql.{Dataset, SaveMode}

class OrderCreateByMenuWriter(ds: Dataset[OrderCreateByMenu]) {

  def writeCsv(): Unit = ds
    .coalesce(1)
    .write
    .option("header","true")
    .partitionBy("dt")
    .mode(SaveMode.Overwrite)
    .csv("menu-stats")

}
