package org.mongo.visualmongopro

import io.javalin.Javalin

fun main() {
  val app = Javalin.create().start()
  app.get("/hello") { ctx -> ctx.result("Hello World") }

  Runtime.getRuntime().addShutdownHook(Thread { app.stop() })
}
