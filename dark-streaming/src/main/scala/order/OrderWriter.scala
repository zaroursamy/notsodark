package order

import com.github.mjakubowski84.parquet4s.ParquetWriter
import order.model.request.OrderRequestInterface.OrderCreateRequest
import org.apache.parquet.hadoop.ParquetFileWriter
import org.apache.parquet.hadoop.metadata.CompressionCodecName
import akka.{Done, NotUsed}
import akka.actor.ActorSystem
import akka.stream.scaladsl.{Sink, Source}
import com.github.mjakubowski84.parquet4s.{Col, ParquetStreams, Path}
import com.typesafe.scalalogging.LazyLogging
import order.streaming.OrderTopics

import scala.concurrent.Future
import scala.concurrent.duration.DurationInt

class OrderWriter(orderCreateRequestIterable: Iterable[OrderCreateRequest], implicit val actorSystem: ActorSystem) extends LazyLogging {

  val writeOptions = ParquetWriter.Options(
    compressionCodecName = CompressionCodecName.SNAPPY,
    writeMode = ParquetFileWriter.Mode.CREATE
  )

  val orderCreateRequestDT: LazyList[OrderCreateDT] = orderCreateRequestIterable.map(OrderCreateDT(_)).to(LazyList)

  def write: Future[Done] = {
    Source(orderCreateRequestDT)
      .via(
        ParquetStreams
          .viaParquet
          .of[OrderCreateDT]
          .partitionBy(Col("dt"))
          .maxCount(writeOptions.rowGroupSize)
          .maxDuration(30.seconds)
          .options(writeOptions)
          .write(Path(OrderTopics.CREATE_ORDERS))
      ).runForeach(order => logger.info(s"Wrote menu ${order.menuId} on dt=${order.dt}..."))
  }


}
