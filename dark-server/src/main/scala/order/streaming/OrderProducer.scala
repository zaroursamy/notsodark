package order.streaming

import org.apache.avro.generic.GenericRecord
import org.apache.kafka.clients.producer.KafkaProducer

object OrderProducer {

  lazy val producer = new KafkaProducer[String, GenericRecord](OrderProducerProperties.producerProps)

}
