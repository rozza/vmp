package org.mongo.visualmongopro.aniket.dao

import org.mongo.visualmongopro.aniket.models.Document
import com.mongodb.ConnectionString
import com.mongodb.client.MongoClients

import com.mongodb.MongoClientSettings
import com.mongodb.client.MongoClient


class FlexibleCollectionDao(private val mongoClient: MongoClient) {

  /**
   * Inserts a document into whatever collection it's a part of.
   */
  fun insertDocument(doc : Document, collection : String) {

    val database = mongoClient.getDatabase("test")
    val collection = database.getCollection(collection)

    val mongodbDocument = org.bson.Document().apply {
      doc.fields.forEach {
        append(it.name, it.value)
      }
    }
    collection.insertOne(mongodbDocument)
  }
}