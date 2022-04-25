package main

import order.{OrderCreateConsumer, OrderCreateConsumerProperties}
import org.apache.avro.generic.GenericRecord
import org.apache.kafka.clients.consumer.KafkaConsumer
import serde.AvroImplicits._

object DarkStreaming extends App {

  val consumer = new KafkaConsumer[String, GenericRecord](OrderCreateConsumerProperties.orderCreateProps)

  OrderCreateConsumer.consumeAndWriteOrderCreateRequests(consumer)

}
