package org.mongo.visualmongopro

import spark.kotlin.Http
import spark.kotlin.ignite

fun main(args: Array<String>) {
  val http: Http = ignite()

  http.get("/hello") {
    "Hello Spark Kotlin!"
  }
}
