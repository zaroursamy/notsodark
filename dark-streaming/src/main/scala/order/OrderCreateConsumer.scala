package order

import com.sksamuel.avro4s.RecordFormat
import com.typesafe.scalalogging.LazyLogging
import order.model.request.OrderRequestInterface.OrderCreateRequest
import org.apache.avro.generic.GenericRecord
import org.apache.kafka.clients.consumer.KafkaConsumer

import java.time.Duration
import scala.jdk.CollectionConverters.IterableHasAsScala

object OrderCreateConsumer extends LazyLogging {

  def consumeOrderCreate(consumer: KafkaConsumer[String, GenericRecord])
                        (implicit recordFormat: RecordFormat[OrderCreateRequest]): Unit = {


    while(true){

      val records: Iterable[OrderCreateRequest] = consumer
        .poll(Duration.ofSeconds(10))
        .asScala
        .map(record ⇒ recordFormat.from(record.value()))

      new OrderWriter(records).write
    }
  }
}
