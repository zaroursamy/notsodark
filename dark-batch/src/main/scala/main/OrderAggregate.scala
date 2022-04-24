package main

import com.typesafe.scalalogging.LazyLogging
import order.{OrderCreateByMenu, OrderCreateByMenuWriter, OrderCreateDT, OrderCreateDTReader}
import org.apache.spark.sql.{Dataset, SparkSession}

import scala.util.Try

object OrderAggregate extends App with LazyLogging {

  implicit val sparkSession: SparkSession = SessionBuilder.sparkSession("OrderAggregate")

  val reader = new OrderCreateDTReader(sparkSession)

  val ds: Dataset[OrderCreateDT] = reader.readParquet

  val orderCreateByMenu: Dataset[OrderCreateByMenu] = OrderCreateByMenu.orderCreateByMenu(ds).cache

  orderCreateByMenu.show()

  Try{
    new OrderCreateByMenuWriter(orderCreateByMenu).writeCsv()
  }.fold(
    {thr ⇒
      logger.warn(thr.getMessage, thr)
    },
    {_ ⇒
      logger.info("succesfully wrote csv")
    }
  )
  sparkSession.stop()

}
