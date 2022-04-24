package model.menu

sealed trait Currency

object Currency{
  case object Gram extends Currency
  case object Unity extends Currency
  case object Litre extends Currency
}
