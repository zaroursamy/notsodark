package order.persistence

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait PersistenceDB[T <: {val id: U}, U] {

  def dbName: String

  var items: LazyList[T] = LazyList.empty

  def saveInDB(item: T): Future[T] = {
    items = items :+ item
    Future(item)
  }

  def fetchAll: Future[LazyList[T]] = Future(items)

  def getById(id: U): Future[Option[T]] = Future(items.find(_.id == id))

}

object PersistenceDB {

  def apply[T <: {val id: U}, U](name: String): PersistenceDB[T, U] = new PersistenceDB[T, U] {
    override def dbName: String = name
  }
}
