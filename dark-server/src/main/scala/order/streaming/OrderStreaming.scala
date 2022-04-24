package order.streaming

import com.sksamuel.avro4s.RecordFormat
import order.model.request.OrderRequestInterface.{OrderCompleteRequest, OrderCreateRequest}
import org.apache.avro.generic.GenericRecord
import org.apache.kafka.clients.producer.{ProducerRecord, RecordMetadata}

import java.util.concurrent.Future

object OrderStreaming {

  def produceOrderCreate(topic: String, orderCreateRequest: OrderCreateRequest)(implicit recordFormat: RecordFormat[OrderCreateRequest]): Future[RecordMetadata] = {
    OrderProducer.producer.send(new ProducerRecord[String, GenericRecord](topic, orderCreateRequest.id.toString, recordFormat.to(orderCreateRequest)))
  }

  def produceOrderComplete(topic: String, orderCompleteRequest: OrderCompleteRequest)(implicit recordFormat: RecordFormat[OrderCompleteRequest]): Future[RecordMetadata] = {
    OrderProducer.producer.send(new ProducerRecord[String, GenericRecord](topic, orderCompleteRequest.id.toString, recordFormat.to(orderCompleteRequest)))
  }
}
