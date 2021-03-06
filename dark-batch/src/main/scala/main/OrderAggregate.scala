package main

import com.typesafe.scalalogging.LazyLogging
import order.{OrderCreateByMenu, OrderCreateByMenuWriter, OrderCreateDT, OrderCreateDTReader}
import org.apache.spark.sql.{Dataset, SparkSession}

import scala.util.Try

object OrderAggregate extends App with LazyLogging {

  implicit val sparkSession: SparkSession = SessionBuilder.sparkSession("OrderAggregate")

  val ds: Dataset[OrderCreateDT] = new OrderCreateDTReader(sparkSession).readParquet

  val orderCreateByMenu: Dataset[OrderCreateByMenu] = OrderCreateByMenu.orderCreateByMenu(ds).cache

  orderCreateByMenu.show()

  Try {
    new OrderCreateByMenuWriter(orderCreateByMenu).writeCsv()
  }.fold(
    { thr ⇒
      logger.warn(thr.getMessage, thr)
    },
    { _ ⇒
      logger.info("Succesfully wrote csv")
    }
  )
  sparkSession.stop()

}
