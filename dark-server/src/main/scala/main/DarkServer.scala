package main


import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import com.sksamuel.avro4s.RecordFormat
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.LazyLogging
import model.restaurant.Restaurant
import order.model.request.OrderRequestInterface.{OrderCompleteRequest, OrderCreateRequest}
import order.model.response.{OrdersCompleted, OrdersCreated}
import order.persistence.PersistenceDB
import restaurant.{RestaurantFetcher, RestaurantNotify}
import spray.json.DefaultJsonProtocol._
import model.OrderRestaurant
import order.streaming.{OrderStreaming, OrderTopics}
import spray.json.RootJsonFormat

import scala.concurrent.Future
import scala.io.StdIn

object DarkServer extends App with LazyLogging {

  logger.info("Starting DarkServer...")

  implicit val system = ActorSystem(Behaviors.empty, "DarkServer")
  implicit val executionContext = system.executionContext

  implicit val orderRequestFormat: RootJsonFormat[OrderCreateRequest] = jsonFormat7(OrderCreateRequest)
  implicit val orderCompletedFormat: RootJsonFormat[OrderCompleteRequest] = jsonFormat2(OrderCompleteRequest)
  implicit val ordersCreatedFormat: RootJsonFormat[OrdersCreated] = jsonFormat1(OrdersCreated)
  implicit val ordersCompletedFormat: RootJsonFormat[OrdersCompleted] = jsonFormat1(OrdersCompleted)
  implicit val restaurantFormat: RootJsonFormat[Restaurant] = jsonFormat3(Restaurant)
  implicit val orderRestaurantFormat: RootJsonFormat[OrderRestaurant] = jsonFormat2(OrderRestaurant)
  implicit val orderCreateRecordFormat: RecordFormat[OrderCreateRequest] = RecordFormat[OrderCreateRequest]
  implicit val orderCompleteRecordFormat: RecordFormat[OrderCompleteRequest] = RecordFormat[OrderCompleteRequest]

  val config = ConfigFactory.load()

  val orderDB = PersistenceDB[OrderCreateRequest, Long]("created-orders")
  val orderCompletedDB = PersistenceDB[OrderCompleteRequest, Long]("completed-orders")
  val restaurantDB = PersistenceDB[Restaurant, String]("restaurants")


  val route = concat(
    post{
      path("create-order"){
        entity(as[OrderCreateRequest]){ orderCreate: OrderCreateRequest ⇒

          val orderSavedInDB: Future[OrderCreateRequest] = orderDB.saveInDB(orderCreate)

          RestaurantNotify.notifyBestRestaurant(new RestaurantFetcher(orderCreate, restaurantDB))

          OrderStreaming.produceOrderCreate(OrderTopics.CREATE_ORDERS, orderCreate)

          onSuccess(orderSavedInDB){
            complete(_)
          }
        }
      }
    },
    post{
      path("complete-order"){
        entity(as[OrderCompleteRequest]){ orderComplete ⇒

          val orderCompletedSaveInDB: Future[OrderCompleteRequest] = orderCompletedDB.saveInDB(orderComplete)
          OrderStreaming.produceOrderComplete(OrderTopics.COMPLETE_ORDERS, orderComplete)

          onSuccess(orderCompletedSaveInDB){
            complete(_)
          }
        }
      }
    },
    post{
      path("create-restaurants"){
        entity(as[Restaurant]){restaurant ⇒

          onSuccess(restaurantDB.saveInDB(restaurant)){
            complete(_)
          }
        }
      }
    },
    get{
      pathPrefix("order" / LongNumber){id ⇒
        val order: Future[Option[OrderCreateRequest]] = orderDB.getById(id)

        onSuccess(order){
          case Some(o) ⇒ complete(o)
          case None ⇒ complete(StatusCodes.NotFound)
        }
      }
    },
    get{
      pathPrefix("created-orders" ){
        onSuccess(orderDB.fetchAll){results ⇒
          results.toList match {
            case Nil ⇒ complete(StatusCodes.NotFound)
            case l ⇒ complete(OrdersCreated(l))
          }
        }
      }
    },
    get{
      pathPrefix("completed-orders" ){
        onSuccess(orderCompletedDB.fetchAll){results ⇒
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
