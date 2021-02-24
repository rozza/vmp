package org.mongo.visualmongopro.aniket

import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder
import io.javalin.core.util.LogUtil
import io.javalin.core.util.RouteOverviewPlugin
import io.javalin.plugin.json.JavalinJson
import kong.unirest.HttpRequestWithBody
import kong.unirest.HttpResponse
import kong.unirest.Unirest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mongo.visualmongopro.aniket.apis.DynamicCollectionsEndPoint
import org.mongo.visualmongopro.aniket.models.Document
import org.mongo.visualmongopro.aniket.models.DocumentField
import org.mongo.visualmongopro.aniket.models.MdbTypes
import org.mongo.visualmongopro.ross.FormsEndPoint
import java.lang.Thread.sleep

class DocumentTest {

  @Test
  fun `create a sample document and send it to storage`() {

    val app = Javalin.create { config ->
      config.registerPlugin(RouteOverviewPlugin("/")) // Shows all routes on "/"
      config.enableDevLogging() // Noisy debug logging - might be helpful
    }.start(7000)

    app.routes {
      ApiBuilder.get("/hello") { ctx -> ctx.result("Hello World") }

      ApiBuilder.path("ross/forms", FormsEndPoint())

      ApiBuilder.path("aniket/access", DynamicCollectionsEndPoint())
    }

    Runtime.getRuntime().addShutdownHook(Thread { app.stop() })

    sleep(15000)

    val field = DocumentField("name", MdbTypes.STRING, "hi")

    val document = Document(listOf(field))

    val q : HttpResponse<String> = Unirest.post("http://localhost:7000/aniket/access/insert")
      .body(document)
      .asString()

    val x : HttpResponse<String> = Unirest.get("http://localhost:7000/aniket/access/about").asString()

    assertThat(x.status).isEqualTo(200)




//    assertThat(q.status).isEqualTo(200)
    app.stop()
  }
}