package restaurant

import com.typesafe.scalalogging.LazyLogging
import model.restaurant.Restaurant
import scalaz.Scalaz.futureInstance
import scalaz._
import scalaz.std.option._
import scalaz.syntax.traverse._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class RestaurantNotify(restaurant: Restaurant) extends LazyLogging {

  def notifyRestaurant: Future[Restaurant] = {
    logger.info(s"Notify restaurant ${restaurant.id}")
    Future(restaurant)
  }

}

object RestaurantNotify{

  def notifyBestRestaurant(restaurantFetcher: RestaurantFetcher): Future[Option[Restaurant]] = restaurantFetcher
    .findBestRestaurant
    .map(_.map(new RestaurantNotify(_).notifyRestaurant).sequence)
    .flatten

}
