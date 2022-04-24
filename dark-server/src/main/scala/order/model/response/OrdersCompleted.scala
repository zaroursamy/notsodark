package order.model.response

import order.model.request.OrderRequestInterface.OrderCompleteRequest

case class OrdersCompleted(orders: List[OrderCompleteRequest])
