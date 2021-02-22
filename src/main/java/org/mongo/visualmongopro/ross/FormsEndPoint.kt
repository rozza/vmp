package org.mongo.visualmongopro.ross

import io.javalin.apibuilder.ApiBuilder
import io.javalin.apibuilder.ApiBuilder.get
import io.javalin.apibuilder.EndpointGroup

class FormsEndPoint : EndpointGroup {
  override fun addEndpoints() {

    ApiBuilder.crud("/:form-id", FormsController())

    get("/about") { ctx ->
      ctx.result("Forms group here..")
    }
  }
}
