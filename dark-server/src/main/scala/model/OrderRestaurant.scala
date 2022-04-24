package model

import model.restaurant.Restaurant
import order.model.request.OrderRequestInterface.OrderCreateRequest

case class OrderRestaurant(order: OrderCreateRequest, restaurant: Option[Restaurant])
