package org.mongo.visualmongopro

import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.get
import io.javalin.apibuilder.ApiBuilder.path
import io.javalin.core.util.RouteOverviewPlugin
import org.mongo.visualmongopro.aniket.apis.DynamicCollectionsEndPoint
import org.mongo.visualmongopro.ross.FormsEndPoint

fun main() {

  val app = Javalin.create { config ->
    config.registerPlugin(RouteOverviewPlugin("/")) // Shows all routes on "/"
    config.enableDevLogging() // Noisy debug logging - might be helpful
  }.start()

  app.routes {
    get("/hello") { ctx -> ctx.result("Hello World") }

    path("ross/forms", FormsEndPoint())

    path("aniket/access", DynamicCollectionsEndPoint())
  }

  Runtime.getRuntime().addShutdownHook(Thread { app.stop() })
}
