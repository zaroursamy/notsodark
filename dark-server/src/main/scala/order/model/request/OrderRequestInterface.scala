package order.model.request

import java.time.Instant

sealed trait OrderRequestInterface

object OrderRequestInterface{

  case class OrderCompleteRequest(id: Long,
                                  timestamp: Long = Instant.now().getEpochSecond) extends OrderRequestInterface

  case class OrderCreateRequest(
                          id: Long,
                          userId: String,
                          latitude: Double,
                          longitude: Double,
                          timestamp: Long = Instant.now().getEpochSecond,
                          menuId: String,
                          price: Double
                        ) extends OrderRequestInterface

}
