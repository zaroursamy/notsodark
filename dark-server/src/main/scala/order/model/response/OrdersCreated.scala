package order.model.response

import order.model.request.OrderRequestInterface.OrderCreateRequest

final case class OrdersCreated(orders: List[OrderCreateRequest])
