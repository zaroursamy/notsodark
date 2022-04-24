package utils

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper

class DistanceTest extends AnyFlatSpec {

  behavior of "DistanceTest"

  "Given two points, calling euclidean should " should "compute right distance" in {

    val (lat1, lon1) = (0, 0)
    val (lat2, lon2) = (0, 1)

    val ExpectedDistance = 1

    Distance.euclidean(lat1, lon1, lat2, lon2) shouldEqual ExpectedDistance
  }

}
