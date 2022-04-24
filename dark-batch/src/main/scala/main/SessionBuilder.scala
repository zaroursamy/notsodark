package main

import org.apache.spark.sql.SparkSession

object SessionBuilder {

  def sparkSession(appName: String): SparkSession = SparkSession
    .builder()
    .master("local[*]")
    .appName(appName)
    .getOrCreate()
}
