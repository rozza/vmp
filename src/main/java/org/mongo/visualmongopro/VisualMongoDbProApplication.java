package org.mongo.visualmongopro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;

// Exclude the built-in providers of MongoDB configuration included in Spring Boot
// This seems like a source of pain for anyone that doesn't want this default
@SpringBootApplication(exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
public class VisualMongoDbProApplication {

  public static void main(String[] args) {
    SpringApplication.run(VisualMongoDbProApplication.class, args);
  }
}
