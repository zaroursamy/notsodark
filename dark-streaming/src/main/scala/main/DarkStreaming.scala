package main

import com.sksamuel.avro4s.RecordFormat
import order.model.request.OrderRequestInterface.OrderCreateRequest
import order.streaming.OrderTopics
import order.{OrderCreateConsumer, OrderCreateConsumerProperties}
import org.apache.avro.generic.GenericRecord
import org.apache.kafka.clients.consumer.KafkaConsumer

import java.util.Collections.singletonList

object DarkStreaming extends App {
  implicit val orderCreateRecordFormat: RecordFormat[OrderCreateRequest] = RecordFormat[OrderCreateRequest]

  val consumer = new KafkaConsumer[String, GenericRecord](OrderCreateConsumerProperties.orderCreateProps)
  consumer.subscribe(singletonList(OrderTopics.CREATE_ORDERS))

  OrderCreateConsumer.consumeOrderCreate(consumer)


}
