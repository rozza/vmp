/*
 * Copyright (c) 2008 - 2013 10gen, Inc. <http://10gen.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.mongo.visualmongopro.graphql;

import com.mongodb.ClientSessionOptions;
import com.mongodb.ReadConcern;
import com.mongodb.ReadPreference;
import com.mongodb.TransactionOptions;
import com.mongodb.WriteConcern;
import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.model.IndexOptions;
import graphql.schema.DataFetcher;
import org.bson.BsonArray;
import org.bson.BsonDocument;
import org.bson.BsonType;
import org.bson.BsonValue;
import org.bson.Document;
import org.bson.codecs.BsonTypeClassMap;
import org.bson.codecs.DocumentCodecProvider;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;
import org.mongo.visualmongopro.graphql.codecs.OffsetDateTimeCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.empty;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Indexes.ascending;
import static com.mongodb.client.model.Indexes.compoundIndex;
import static java.util.Arrays.asList;

@Component
public class CollectionDataFetcher {
  private final MongoCollection<Document> collectionMetadata;
  private final MongoClient client;
  private final ClientSessionOptions clientSessionOptions;

  public CollectionDataFetcher(@Autowired MongoClient client) {
    this.client = client;
    MongoDatabase visualMongoDBProDatabase = client.getDatabase("visualmongodbpro");
    this.collectionMetadata =
        visualMongoDBProDatabase
            .getCollection("collection.metadata")
            .withCodecRegistry(
                CodecRegistries.fromRegistries(
                    CodecRegistries.fromCodecs(new OffsetDateTimeCodec()),
                    CodecRegistries.fromProviders(
                        new DocumentCodecProvider(
                            new BsonTypeClassMap(
                                Map.of(BsonType.DATE_TIME, OffsetDateTime.class)))),
                    visualMongoDBProDatabase.getCodecRegistry()));
    clientSessionOptions =
        ClientSessionOptions.builder()
            .causallyConsistent(true)
            .defaultTransactionOptions(
                TransactionOptions.builder()
                    .readPreference(ReadPreference.primary())
                    .readConcern(ReadConcern.MAJORITY)
                    .writeConcern(WriteConcern.MAJORITY)
                    .maxCommitTime(1L, TimeUnit.SECONDS)
                    .build())
            .build();
  }

  private static Document cleanType(String type) {
    Document typeDoc = new Document("bson_type", type);
    String formType = type;
    switch (type) {
      case "date_time" -> {
        formType = "string";
        typeDoc.append("format", "date-time");
      }
      case "int32" -> formType = "integer";
      case "double", "decimal128" -> formType = "number";
      case "object_id" -> formType = "string";
    }
    return typeDoc.append("type", formType);
  }

  private static String cleanKey(String key) {
    String s = key.replaceAll("[_-]", " ");
    s = Arrays.stream(s.split("(?=[A-Z])"))
        .map(st -> st.substring(0, 1).toUpperCase() + st.substring(1))
        .collect(Collectors.joining(" "));
    s = Arrays.stream(s.trim().split(" "))
        .map(st -> st.substring(0, 1).toUpperCase() + st.substring(1))
        .collect(Collectors.joining(" "));
    return s.equals("Id") ? "ID" : s;
  }

  @PostConstruct
  public void init() {
    collectionMetadata.drop();
    collectionMetadata.createIndex(compoundIndex(ascending("databaseName", "collectionName")), new IndexOptions().unique(true));
    collectionMetadata.insertMany(
        asList(
            new Document(Map.of(
                "databaseName", "test",
                "collectionName", "test1",
                "jsonSchema", Document.parse("{" +
                    "  \"title\": \"A registration form\"," +
                    "  \"description\": \"A simple form example.\"," +
                    "  \"type\": \"object\"," +
                    "  \"required\": [" +
                    "    \"firstName\"," +
                    "    \"lastName\"" +
                    "  ]," +
                    "  \"properties\": {" +
                    "    \"firstName\": {" +
                    "      \"type\": \"string\"," +
                    "      \"title\": \"First name\"," +
                    "      \"default\": \"Chuck\"" +
                    "    }," +
                    "    \"lastName\": {" +
                    "      \"type\": \"string\"," +
                    "      \"title\": \"Last name\"" +
                    "    }," +
                    "    \"telephone\": {" +
                    "      \"type\": \"string\"," +
                    "      \"title\": \"Telephone\"," +
                    "      \"minLength\": 10" +
                    "    }" +
                    "  }" +
                    "}"),
                "isSchemaAutoGenerated", false,
                "uiSchema", Document.parse("{" +
                    "  \"firstName\": {" +
                    "    \"ui:autofocus\": true," +
                    "    \"ui:emptyValue\": \"\"," +
                    "    \"ui:autocomplete\": \"family-name\"" +
                    "  }," +
                    "  \"lastName\": {" +
                    "    \"ui:emptyValue\": \"\"," +
                    "    \"ui:autocomplete\": \"given-name\"" +
                    "  }," +
                    "  \"age\": {" +
                    "    \"ui:widget\": \"updown\"," +
                    "    \"ui:title\": \"Age of person\"," +
                    "    \"ui:description\": \"(earthian year)\"" +
                    "  }," +
                    "  \"bio\": {" +
                    "    \"ui:widget\": \"textarea\"" +
                    "  }," +
                    "  \"password\": {" +
                    "    \"ui:widget\": \"password\"," +
                    "    \"ui:help\": \"Hint: Make it strong!\"" +
                    "  }," +
                    "  \"date\": {" +
                    "    \"ui:widget\": \"alt-datetime\"" +
                    "  }," +
                    "  \"telephone\": {" +
                    "    \"ui:options\": {" +
                    "      \"inputType\": \"tel\"" +
                    "    }" +
                    "  }" +
                    "}")
            ))
        ));

    List<Document> documents = scanCollectionMetadata(client);
    if (!documents.isEmpty()) {
      collectionMetadata.insertMany(documents);
    }
  }

  private List<Document> scanCollectionMetadata(MongoClient client) {
    List<Document> databases = client.listDatabases().into(new ArrayList<>());
    List<String> dbs = databases.stream().filter(db -> {
      List<String> admin_dbs = Arrays.asList("admin", "config", "local", "visualmongodbpro");
      return !admin_dbs.contains(db.getString("name"));
    }).map(doc -> doc.getString("name")).collect(Collectors.toList());

    List<Document> metadata = new ArrayList<>();

    for (String dbString : dbs) {
      MongoDatabase db = client.getDatabase(dbString);

      for (String coll : db.listCollectionNames()) {
        BsonDocument doc = db.getCollection(coll, BsonDocument.class).find().first();
        Document metadataDoc = new Document("databaseName", dbString).append("collectionName", coll).append("isSchemaAutoGenerated", true);
        metadataDoc.append("jsonSchema", extractJsonSchemaFromDoc(doc));
        metadata.add(metadataDoc);
      }
    }
    return metadata;
  }

  private Document extractJsonSchemaFromDoc(BsonDocument doc) {
    Document propsContent = new Document();
    Document schema = new Document("type", "object").append("properties", propsContent);

    for (Map.Entry<String, BsonValue> entry : doc.entrySet()) {
      String key = entry.getKey();
      BsonType bsonType = entry.getValue().getBsonType();
      if (!bsonType.isContainer()) {
        Document content = cleanType(bsonType.toString().toLowerCase());
        content.append("title", cleanKey(key));
        propsContent.append(key, content);
      } else if (bsonType.equals(BsonType.ARRAY)) {
        BsonArray array = entry.getValue().asArray();
        if (array.size() > 0) { // todo ignoring empty arrays...
          BsonValue first = array.get(0);
          if (first.getBsonType().equals(BsonType.DOCUMENT)) {
            Document content = cleanType(bsonType.toString().toLowerCase());
            content.append("title", cleanKey(key)).append("items", extractJsonSchemaFromDoc(first.asDocument()));
            propsContent.append(key, content);
          } else {
            Document content = cleanType(bsonType.toString().toLowerCase());
            content.append("title", cleanKey(key)).append("items", cleanType(first.getBsonType().toString().toLowerCase()));
            propsContent.append(key, content);
          }
        }
      } else if (bsonType.equals(BsonType.DOCUMENT)) {
        propsContent.append(key, extractJsonSchemaFromDoc(entry.getValue().asDocument()).append("title", cleanKey(key)));
      }
    }
    return schema;
  }

  public DataFetcher<List<Map<String, Object>>> getCollectionsDataFetcher() {
    return dataFetchingEnvironment ->
        find(empty()).into(new ArrayList<>());
  }

  public DataFetcher<List<Map<String, Object>>> getCollectionsByDatabaseDataFetcher() {
    return dataFetchingEnvironment ->
        find(eq("databaseName", dataFetchingEnvironment.<String>getArgument("databaseName")))
            .into(new ArrayList<>());
  }

  public DataFetcher<Map<String, Object>> getCollectionsByNamespaceDataFetcher() {
    return dataFetchingEnvironment ->
        find(and(
            eq("databaseName", dataFetchingEnvironment.<String>getArgument("databaseName")),
            eq("collectionName", dataFetchingEnvironment.<String>getArgument("collectionName"))))
            .first();
  }

  DataFetcher<ObjectId> createDocumentDataFetcher() {
    return dataFetchingEnvironment -> {
      try (ClientSession session = client.startSession(clientSessionOptions)) {
        return session.withTransaction(() -> client.getDatabase(dataFetchingEnvironment.getArgument("databaseName"))
              .getCollection(dataFetchingEnvironment.getArgument("collectionName"))
              .insertOne(session, Document.parse(dataFetchingEnvironment.getArgument("json")))
              .getInsertedId().asObjectId().getValue());
      }
    };
  }

  @NotNull
  private MongoIterable<Document> find(Bson filter) {
    return collectionMetadata.find(filter)
        .map(document -> {
          if (document.containsKey("jsonSchema")) {
            document.put("jsonSchema", document.get("jsonSchema", Document.class).toJson());
          }
          if (document.containsKey("uiSchema")) {
            document.put("uiSchema", document.get("uiSchema", Document.class).toJson());
          }
          return document;
        });
  }
}
