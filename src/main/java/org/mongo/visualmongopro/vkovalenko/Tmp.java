package org.mongo.visualmongopro.vkovalenko;

import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mongodb.ClientSessionOptions;
import com.mongodb.ReadConcern;
import com.mongodb.ReadPreference;
import com.mongodb.TransactionOptions;
import com.mongodb.WriteConcern;
import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoClient;

@Component
final class Tmp {
  Logger logger = LoggerFactory.getLogger(Tmp.class);
  private final MongoClient mongoClient;

  @Autowired
  private Tmp(final MongoClient mongoClient) {
    this.mongoClient = mongoClient;
  }

  @PostConstruct
  private void postConstruct() {
    try (ClientSession clientSession =
        mongoClient.startSession(
            ClientSessionOptions.builder()
                .causallyConsistent(true)
                .defaultTransactionOptions(
                    TransactionOptions.builder()
                        .readPreference(ReadPreference.primary())
                        .readConcern(ReadConcern.MAJORITY)
                        .writeConcern(WriteConcern.MAJORITY)
                        .maxCommitTime(1L, TimeUnit.SECONDS)
                        .build())
                .build())) {
            logger.info(
                "Databases: {}",
                StreamSupport.stream(mongoClient.listDatabaseNames(clientSession).spliterator(),
       false)
                    .collect(Collectors.toList()));
    }
  }
}
