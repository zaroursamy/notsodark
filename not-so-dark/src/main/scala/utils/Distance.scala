package utils

import math.{sqrt, pow}

object Distance {

  def euclidean(x1: Double, y1: Double, x2: Double, y2: Double): Double = sqrt(pow(x1 - x2, 2) + pow(y1 - y2, 2))
}
