package order

import akka.actor.ActorSystem
import com.sksamuel.avro4s.RecordFormat
import com.typesafe.scalalogging.LazyLogging
import order.model.request.OrderRequestInterface.OrderCreateRequest
import order.streaming.OrderTopics
import org.apache.avro.generic.GenericRecord
import org.apache.kafka.clients.consumer.KafkaConsumer

import java.time.Duration
import java.util.Collections.singletonList
import scala.jdk.CollectionConverters.IterableHasAsScala

object OrderCreateConsumer extends LazyLogging {

  def consumeAndWriteOrderCreateRequests(consumer: KafkaConsumer[String, GenericRecord])
                                        (implicit recordFormat: RecordFormat[OrderCreateRequest]): Unit = {

    consumer.subscribe(singletonList(OrderTopics.CREATE_ORDERS))

    implicit val actorSystem: ActorSystem = ActorSystem()

    while (true) {

      val records: Iterable[OrderCreateRequest] = consumer
        .poll(Duration.ofSeconds(10))
        .asScala
        .map(record ⇒ recordFormat.from(record.value()))

      new OrderWriter(records, actorSystem).write
    }
  }
}
