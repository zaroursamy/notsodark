package main

import order.{OrderCreateByMenu, OrderCreateByMenuWriter, OrderCreateDT, OrderCreateDTReader}
import org.apache.spark.sql.{Dataset, SparkSession}

object OrderAggregate extends App {

  implicit val sparkSession: SparkSession = SessionBuilder.sparkSession("OrderAggregate")

  val reader = new OrderCreateDTReader(sparkSession)

  val ds: Dataset[OrderCreateDT] = reader.readParquet

  val orderCreateByMenu: Dataset[OrderCreateByMenu] = OrderCreateByMenu.orderCreateByMenu(ds)

  new OrderCreateByMenuWriter(orderCreateByMenu).writeCsv()
}
