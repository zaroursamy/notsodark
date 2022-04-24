package order.streaming
import io.confluent.kafka.serializers.KafkaAvroSerializer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer

import java.util.Properties

object OrderProducerProperties {

  lazy val producerPropsMap: Map[String, AnyRef] = {

    Map(
      ProducerConfig.BOOTSTRAP_SERVERS_CONFIG -> "localhost:9092",
      ProducerConfig.CLIENT_ID_CONFIG -> "orderProducer",
      ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG -> classOf[KafkaAvroSerializer].getCanonicalName,
      ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG -> classOf[StringSerializer].getCanonicalName,
      ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG -> "true",
      "schema.registry.url" -> s"http://localhost:8081"
    )

  }

  lazy val producerProps: Properties = {
    val props = new Properties()

    producerPropsMap foreach { case (k, v) ⇒ props.put(k, v) }

    props
  }

}
