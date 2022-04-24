package restaurant

import model.restaurant.Restaurant
import order.model.request.OrderRequestInterface.OrderCreateRequest
import order.persistence.PersistenceDB
import utils.Distance

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class RestaurantFetcher(orderCreateRequest: OrderCreateRequest, persistenceDB: PersistenceDB[Restaurant, String]) {

  private def euclideanFromRestaurant(restaurant: Restaurant): Double =
    Distance.euclidean(restaurant.latitude, restaurant.longitude, orderCreateRequest.latitude, orderCreateRequest.longitude)

  def findBestRestaurant: Future[Option[Restaurant]] = getClosestRestaurants
    .map{
      _.foldLeft(Option.empty[Restaurant]){case (acc, restaurant) ⇒
        val bestRestaurant = acc
          .map(oldRest ⇒
            if(euclideanFromRestaurant(oldRest) < euclideanFromRestaurant(restaurant)) oldRest
            else restaurant
          )
          .getOrElse(restaurant)

        Some(bestRestaurant)
      }
    }


  def getClosestRestaurants: Future[LazyList[Restaurant]] = persistenceDB
    .fetchAll
    .map{
      _.filter{case Restaurant(_, lat, lon) ⇒
        Distance.euclidean(lat, lon, orderCreateRequest.latitude, orderCreateRequest.longitude) < 4
    }

  }

}