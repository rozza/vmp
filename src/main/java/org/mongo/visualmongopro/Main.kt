package org.mongo.visualmongopro

import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.get
import io.javalin.apibuilder.ApiBuilder.path
import org.mongo.visualmongopro.ross.FormsEndPoint

fun main() {

  val app = Javalin.create().start()

  app.config.enableDevLogging()

  app.routes {
    get("/hello") { ctx -> ctx.result("Hello World") }

    path("ross/forms", FormsEndPoint())
  }

  Runtime.getRuntime().addShutdownHook(Thread { app.stop() })
}
