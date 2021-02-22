package org.mongo.visualmongopro.controllers

import io.javalin.apibuilder.CrudHandler
import io.javalin.http.Context

class FormsController : CrudHandler {

  override fun create(ctx: Context) {
    TODO("Not yet implemented")
  }

  override fun delete(ctx: Context, resourceId: String) {
    TODO("Not yet implemented")
  }

  override fun getAll(ctx: Context) {
    ctx.result("OK")
  }

  override fun getOne(ctx: Context, resourceId: String) {
    TODO("Not yet implemented")
  }

  override fun update(ctx: Context, resourceId: String) {
    TODO("Not yet implemented")
  }
}
