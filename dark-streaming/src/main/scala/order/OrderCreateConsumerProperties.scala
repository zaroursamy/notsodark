package order

import io.confluent.kafka.serializers.KafkaAvroDeserializer
import order.streaming.OrderTopics
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer

import java.util.Properties

object OrderCreateConsumerProperties {

  lazy val orderCreateProps: Properties = {
    val props = new Properties()
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
    props.put(ConsumerConfig.GROUP_ID_CONFIG, OrderTopics.CREATE_ORDERS)
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, classOf[StringDeserializer].getName)
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, classOf[KafkaAvroDeserializer].getName)
    props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true")
    props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest")
    props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "1000")
    props.put("schema.registry.url", "http://localhost:8081")
    props
  }
}
