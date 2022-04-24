package order

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper

import java.sql.Date

class OrderCreateByMenuTest extends AnyFlatSpec {

  val defaultOrder: OrderCreateDT = OrderCreateDT(
    id = 1,
    userId = "samy",
    latitude = 0,
    longitude = 0,
    timestamp = 0,
    menuId = "kebab",
    price = 10,
    dt = "1970-01-01"
  )

  val dt: Date = Date.valueOf(defaultOrder.dt)


  "Given orders created, it" should "aggregate by menu" in {

    val orderCreateByMenuSeq = Iterator(
      defaultOrder,
      defaultOrder.copy(id = 2, userId = "samia")
    )

    val expectedMenu = OrderCreateByMenu(
      menuId = defaultOrder.menuId,
      totalPrice = 20,
      nbUsers = 2,
      dt = dt
    )

    val computedMenu: OrderCreateByMenu = OrderCreateByMenu.createMenuStats((defaultOrder.menuId, defaultOrder.dt), orderCreateByMenuSeq)

    expectedMenu.nbUsers shouldEqual computedMenu.nbUsers
    expectedMenu.totalPrice shouldEqual computedMenu.totalPrice

  }

}
