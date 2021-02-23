package org.mongo.visualmongopro.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.bson.UuidRepresentation;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

@Configuration
public class MongoClientAppConfig {

  // I'm not clear how this field can be final and still set by the Value annotation, but it works
  @Value("#{systemEnvironment['MONGODB_URI']}")
  private final String uri = "mongodb://localhost";

  @Bean
  public MongoClient mongoClient() {
    return MongoClients.create(
        MongoClientSettings.builder()
            .applicationName("Visual MongoDB Pro")
            .applyConnectionString(new ConnectionString(uri))
            .uuidRepresentation(UuidRepresentation.STANDARD)
            .build());
  }
}
