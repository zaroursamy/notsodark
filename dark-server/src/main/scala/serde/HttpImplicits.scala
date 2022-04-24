package serde

import model.restaurant.Restaurant
import order.model.request.OrderRequestInterface.{OrderCompleteRequest, OrderCreateRequest}
import order.model.response.{OrdersCompleted, OrdersCreated}
import spray.json.DefaultJsonProtocol._
import spray.json.RootJsonFormat

object HttpImplicits {
  implicit val orderRequestFormat: RootJsonFormat[OrderCreateRequest] = jsonFormat7(OrderCreateRequest)
  implicit val orderCompletedFormat: RootJsonFormat[OrderCompleteRequest] = jsonFormat2(OrderCompleteRequest)
  implicit val ordersCreatedFormat: RootJsonFormat[OrdersCreated] = jsonFormat1(OrdersCreated)
  implicit val ordersCompletedFormat: RootJsonFormat[OrdersCompleted] = jsonFormat1(OrdersCompleted)
  implicit val restaurantFormat: RootJsonFormat[Restaurant] = jsonFormat3(Restaurant)
}
