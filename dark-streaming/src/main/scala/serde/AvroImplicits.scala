package serde

import com.sksamuel.avro4s.RecordFormat
import order.model.request.OrderRequestInterface.OrderCreateRequest

object AvroImplicits {
  implicit val orderCreateRecordFormat: RecordFormat[OrderCreateRequest] = RecordFormat[OrderCreateRequest]

}
