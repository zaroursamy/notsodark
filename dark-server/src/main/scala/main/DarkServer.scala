package main


import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.LazyLogging
import model.restaurant.Restaurant
import order.model.request.OrderRequestInterface.{OrderCompleteRequest, OrderCreateRequest}
import order.model.response.{OrdersCompleted, OrdersCreated}
import order.persistence.PersistenceDB
import order.streaming.{OrderStreaming, OrderTopics}
import restaurant.{RestaurantFetcher, RestaurantNotify}
import serde.HttpImplicits._
import serde.KafkaImplicits._

import scala.concurrent.Future
import scala.io.StdIn

object DarkServer extends App with LazyLogging {

  logger.info("Starting DarkServer...")

  implicit val system = ActorSystem(Behaviors.empty, "DarkServer")
  implicit val executionContext = system.executionContext

  val config = ConfigFactory.load()

  val orderDB = PersistenceDB[OrderCreateRequest, Long]("created-orders")
  val orderCompletedDB = PersistenceDB[OrderCompleteRequest, Long]("completed-orders")
  val restaurantDB = PersistenceDB[Restaurant, String]("restaurants")


  val route = concat(
    post {
      path("create-order") {
        entity(as[OrderCreateRequest]) { orderCreate: OrderCreateRequest ⇒

          val orderSavedInDB: Future[OrderCreateRequest] = orderDB.saveInDB(orderCreate)

          RestaurantNotify.notifyBestRestaurant(new RestaurantFetcher(orderCreate, restaurantDB))

          OrderStreaming.produceOrderCreate(OrderTopics.CREATE_ORDERS, orderCreate)

          onSuccess(orderSavedInDB) {
            complete(_)
          }
        }
      }
    },
    post {
      path("complete-order") {
        entity(as[OrderCompleteRequest]) { orderComplete ⇒

          val orderCompletedSaveInDB: Future[OrderCompleteRequest] = orderCompletedDB.saveInDB(orderComplete)
          OrderStreaming.produceOrderComplete(OrderTopics.COMPLETE_ORDERS, orderComplete)

          onSuccess(orderCompletedSaveInDB) {
            complete(_)
          }
        }
      }
    },
    post {
      path("create-restaurants") {
        entity(as[Restaurant]) { restaurant ⇒

          onSuccess(restaurantDB.saveInDB(restaurant)) {
            complete(_)
          }
        }
      }
    },
    get {
      pathPrefix("order" / LongNumber) { id ⇒
        val order: Future[Option[OrderCreateRequest]] = orderDB.getById(id)

        onSuccess(order) {
          case Some(o) ⇒ complete(o)
          case None ⇒ complete(StatusCodes.NotFound)
        }
      }
    },
    get {
      pathPrefix("created-orders") {
        onSuccess(orderDB.fetchAll) { results ⇒
          results.toList match {
            case Nil ⇒ complete(StatusCodes.NotFound)
            case l ⇒ complete(OrdersCreated(l))
          }
        }
      }
    },
    get {
      pathPrefix("completed-orders") {
        onSuccess(orderCompletedDB.fetchAll) { results ⇒
          results.toList match {
            case Nil ⇒ complete(StatusCodes.NotFound)
            case l ⇒ complete(OrdersCompleted(l))
          }
        }
      }
    }
  )


  val bindingFuture = Http().newServerAt("localhost", 8080).bind(route)
  logger.info(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
  StdIn.readLine() // let it run until user presses return
  bindingFuture
    .flatMap(_.unbind()) // trigger unbinding from the port
    .onComplete(_ => system.terminate()) // and shutdown when done
}
