package order.persistence

import model.order.Order
import org.scalatest.matchers.must.Matchers.have
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class OrderDBTest extends org.scalatest.flatspec.AnyFlatSpec {

  val orderDB = new OrderCreateDB("test")

  "Given an order, when calling saveOrderInDB, it" should "insert the order in database" in {
    val order = Order(
      0,
      "samy",
      0,
      0,
      0,
      "kebab",
      10
    )

    orderDB.saveOrderInDB(order)

    val orders = Await.result(orderDB.fetchAllOrders(), Duration.Inf)
    orders.toList should have size 1

  }
}
