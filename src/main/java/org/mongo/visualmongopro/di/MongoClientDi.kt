package org.mongo.visualmongopro.di

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import java.util.logging.Level
import java.util.logging.Logger

class MongoClientDi {

  fun createMongoClient() : MongoClient {

    val connString = ConnectionString(
      "mongodb+srv://someuser:jKV6ajDU1qQqbkTN@cluster0.9r3uo.mongodb.net/test?w=majority"
    )

    val settings = MongoClientSettings.builder()
      .applyConnectionString(connString)
      .retryWrites(true)
      .build()

    val mongoClient = MongoClients.create(settings)

    val logger: Logger = Logger.getLogger("org.mongodb.driver")
    logger.setLevel(Level.ALL)

    return mongoClient
  }
}