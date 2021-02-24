package org.mongo.visualmongopro.config;

import org.springframework.beans.factory.annotation.Autowired;
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

  private final String uri;

  @Autowired
  MongoClientAppConfig(@Value("#{systemEnvironment['MONGODB_URI']}:mongodb://localhost") final String uri) {
    this.uri = uri;
  }

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
