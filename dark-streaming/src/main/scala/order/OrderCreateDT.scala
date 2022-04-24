package order

import order.model.request.OrderRequestInterface.OrderCreateRequest

import java.sql.Date
import java.text.SimpleDateFormat

case class OrderCreateDT(
                          id: Long,
                          userId: String,
                          latitude: Double,
                          longitude: Double,
                          timestamp: Long,
                          menuId: String,
                          price: Double,
                          dt: String
                        )

object OrderCreateDT {
  def apply(orderCreateRequest: OrderCreateRequest): OrderCreateDT = {
    import orderCreateRequest._

    val dtStr: String = new SimpleDateFormat("yyyy-MM-dd").format(new Date(timestamp))

    new OrderCreateDT(
      id = id,
      userId = userId,
      latitude = latitude,
      longitude = longitude,
      timestamp = timestamp,
      menuId = menuId,
      price = price,
      dt = dtStr
    )
  }
}
