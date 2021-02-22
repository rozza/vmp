package org.mongo.visualmongopro

import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.crud
import io.javalin.apibuilder.ApiBuilder.get
import org.mongo.visualmongopro.controllers.FormsController

fun main() {

  val app = Javalin.create().start()

  app.routes {
    get("/hello") { ctx -> ctx.result("Hello World") }

    crud("/forms/:form-id", FormsController())
  }

  Runtime.getRuntime().addShutdownHook(Thread { app.stop() })
}
