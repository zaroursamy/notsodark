package order

import org.apache.spark.sql.Dataset

import java.sql.Date

case class OrderCreateByMenu(
                           menuId: String,
                           totalPrice: Double,
                           nbUsers: Int,
                           dt: Date
                           )

object OrderCreateByMenu{

  def createMenuStats(menuDt: (String,  String), data: Iterator[OrderCreateDT]): OrderCreateByMenu ={
    val (menuId, dt) = menuDt
    val listData = data.toList

    val totalPrice = listData.map(_.price).sum
    val nbUsers = listData.map(_.userId).distinct.size

    OrderCreateByMenu(
      menuId,
      totalPrice,
      nbUsers,
      Date.valueOf(dt)
    )
  }

  def orderCreateByMenu(ds: Dataset[OrderCreateDT]): Dataset[OrderCreateByMenu] = {
    import ds.sparkSession.implicits._

    ds
      .groupByKey(k ⇒ (k.menuId, k.dt))
      .mapGroups(createMenuStats)
      .cache
  }
}
