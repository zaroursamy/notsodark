package serde

import com.sksamuel.avro4s.RecordFormat
import order.model.request.OrderRequestInterface.{OrderCompleteRequest, OrderCreateRequest}

object KafkaImplicits {
  implicit val orderCreateRecordFormat: RecordFormat[OrderCreateRequest] = RecordFormat[OrderCreateRequest]
  implicit val orderCompleteRecordFormat: RecordFormat[OrderCompleteRequest] = RecordFormat[OrderCompleteRequest]

}
