package org.mongo.visualmongopro.aniket.apis

import io.javalin.apibuilder.ApiBuilder
import io.javalin.apibuilder.EndpointGroup
import org.mongo.visualmongopro.aniket.dao.FlexibleCollectionDao
import org.mongo.visualmongopro.aniket.models.Document
import org.mongo.visualmongopro.aniket.models.DocumentField
import org.mongo.visualmongopro.aniket.models.MdbTypes
import org.mongo.visualmongopro.di.MongoClientDi
import java.util.logging.Level
import java.util.logging.Logger

class DynamicCollectionsEndPoint : EndpointGroup {

  val dao = FlexibleCollectionDao(MongoClientDi().createMongoClient())

  override fun addEndpoints() {

    ApiBuilder.post("/insert") { ctx ->
      val doc = ctx.body<Document>()
      dao.insertDocument(doc, "idunno")
      ctx.status(200)
    }

    ApiBuilder.get("/about") { ctx ->
      val doc = Document(listOf(DocumentField("name", MdbTypes.STRING, "hi")))
      dao.insertDocument(doc, "idunno")
      ctx.result("Dynamic collections here")
    }

  }
}
