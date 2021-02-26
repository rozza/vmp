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

import com.mongodb.*;
import com.mongodb.client.*;
import com.mongodb.client.model.*;
import graphql.schema.DataFetcher;
import org.bson.*;
import org.bson.codecs.BsonTypeClassMap;
import org.bson.codecs.DocumentCodecProvider;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.conversions.Bson;
import org.bson.json.JsonMode;
import org.bson.json.JsonWriterSettings;
import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;
import org.mongo.visualmongopro.graphql.codecs.OffsetDateTimeCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Indexes.ascending;
import static com.mongodb.client.model.Indexes.compoundIndex;
import static java.util.Arrays.asList;

@Component
public class CollectionDataFetcher {

  private final static Logger LOGGER = LoggerFactory.getLogger(CollectionDataFetcher.class);
  private static final FindOneAndReplaceOptions replaceDocumentOptions = new FindOneAndReplaceOptions()
      .upsert(false)
      .returnDocument(ReturnDocument.AFTER)
      .projection(Projections.include("_id"));
  private static final ClientSessionOptions clientSessionOptions = ClientSessionOptions.builder()
      .causallyConsistent(true)
      .defaultTransactionOptions(
          TransactionOptions.builder()
              .readPreference(ReadPreference.primary())
              .readConcern(ReadConcern.MAJORITY)
              .writeConcern(WriteConcern.MAJORITY)
              .maxCommitTime(1L, TimeUnit.SECONDS)
              .build())
      .build();

  private final MongoCollection<Document> collectionMetadata;
  private final MongoClient client;

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
    if (!Boolean.parseBoolean(System.getProperty("CREATE_METADATA_COLLECTION", "true"))) {
      LOGGER.info("Skipping creating metadata collection");
      return;
    }
    importPainPoints();
    collectionMetadata.drop();
    collectionMetadata.createIndex(compoundIndex(ascending("databaseName", "collectionName")), new IndexOptions().unique(true));
    collectionMetadata.insertMany(scanCollectionMetadata(client));
  }

  private void importPainPoints() {
    MongoCollection<Document> painPoints = client.getDatabase("pain").getCollection("points");
    painPoints.drop();
    List<Document> list = new ArrayList<>();
    list.add(new Document("description", "Too many clicks to find out how to install.\nHow to install / maven coordinates are too many " +
        "clicks.\nAlso not clear from the docs menu what to install or why.\nThe index page could add short cuts for newbies.").append(
        "tags", asList("documentation", "install")));
    list.add(new Document("description", "Only maven and gradle install copy and pastes").append("tags", asList("documentation",
        "tooling", "install")));
    list.add(new Document("description", "Driver docs has the wrong version").append("tags", asList("documentation")));
    list.add(new Document("description", "It's not obvious how to externalize all or part of the client settings when using Spring. All the examples I found embed the connection string in code, which is obviously not what you would want to do.").append("tags", asList("documentation")));
    list.add(new Document("description", "It's not clear that mongo-java-driver has been superseded by mongodb-driver-sync. Perhaps we could update it's description when we release 3.12.9 to point to the new stuff").append("tags", asList("documentation")));
    list.add(new Document("description", "Could use a Document.of set of methods that are equivalent to Map.of").append("tags", asList(
        "documentation", "sugar", "code")));
    list.add(new Document("description", "Builders are hard to find").append("tags", asList("documentation", "sugar")));
    list.add(new Document("description", "Not easy to do * imports on builders").append("tags", asList("documentation", "coding")));
    list.add(new Document("description", "into(new ArrayList<>()); is a bit long and could be more sexy. Why not just toList(); ?").append("tags", asList("documentation", "sugar", "code")));
    list.add(new Document("description", "Shortcuts like \"Aggregate.sample()\" are hard to find.").append("tags", asList("documentation"
        , "code")));
    list.add(new Document("description", "Would be nice to have an easy way to retrieve the schema of a collection directly from the " +
        "driver. Just like Compass basically but programmatically.").append("tags", asList("new feature")));
    list.add(new Document("description", "Exception in thread \"main\" org.bson.codecs.configuration.CodecConfigurationException: Can't " +
        "find a codec for class org.bson.BsonType. \tat org.bson.internal.CodecCache.getOrThrow(CodecCache.java:57) \tat org.bson" +
        ".internal.ProvidersCodecRegistry.get(ProvidersCodecRegistry.java:64) \tat org.bson.internal.ProvidersCodecRegistry.get" +
        "(ProvidersCodecRegistry.java:39) \tat org.bson.codecs.DocumentCodec.writeValue(DocumentCodec.java:202) \tat org.bson.codecs" +
        ".DocumentCodec.writeMap(DocumentCodec.java:217) \tat org.bson.codecs.DocumentCodec.writeValue(DocumentCodec.java:200) \tat org" +
        ".bson.codecs.DocumentCodec.writeMap(DocumentCodec.java:217) \tat org.bson.codecs.DocumentCodec.writeValue(DocumentCodec" +
        ".java:200) \tat org.bson.codecs.DocumentCodec.writeMap(DocumentCodec.java:217) \tat org.bson.codecs.DocumentCodec.encode(DocumentCodec.java:159) \tat org.bson.codecs.DocumentCodec.encode(DocumentCodec.java:46) \tat org.bson.Document.toJson(Document.java:440) \tat org.bson.Document.toJson(Document.java:413) \tat com.mongodb.quickstart.Connection.main(Connection.java:51)").append("tags", asList("documentation", "code")));
    list.add(new Document("description", "The driver documentation does not explain the difference between TransactionOptions.getMaxCommitTime and SocketSettings.getReadTimeout, and why getMaxCommitTime may be needed.").append("tags", asList("documentation")));
    list.add(new Document("description", "The docs state that TransactionBody \"should be idempotent\". This is misleading because the MongoDB docs https://docs.mongodb.com/manual/core/transactions-in-applications/#callback-api show an example where insertOne (not an idempotent operation) is used in a transaction.").append("tags", asList("documentation")));
    list.add(new Document("description",
        "The code that cloud.mongodb.com suggests when using the java driver is outdated.\nThe correct code is on the docs:\nhttps://docs" +
            ".mongodb.com/drivers/java/\n\nimport com.mongodb.ConnectionString;\nimport com.mongodb.client.MongoClients;\nimport com" +
            ".mongodb.client.MongoClient;\nimport com.mongodb.client.MongoDatabase;\nimport com.mongodb.MongoClientSettings;\n\n// .." +
            ".\nConnectionString connString = new ConnectionString" +
            "(\n\"mongodb+srv://<username>:<password>@<cluster-address>/test?w=majority\"\n);\nMongoClientSettings settings = " +
            "MongoClientSettings.builder()\n.applyConnectionString(connString)\n.retryWrites(true)\n.build();\nMongoClient mongoClient = " +
            "MongoClients.create(settings);\nMongoDatabase database = mongoClient.getDatabase(\"test\");").append("tags", asList("documentation", "code")));
    list.add(new Document("description", "Hard time figuring out how to log things\nIt wasn’t clear how to enable or control logging and " +
        "only a stack overflow post helped.\nGoogling “mongodb java driver enable logging” didn’t show me any page of docs that was " +
        "helpful.\nSuggest adding logging to the getting started page of the driver.").append("tags", asList("documentation", "code",
        "log")));
    list.add(new Document("description", "Monitor threads\nThis is unclear to developers, why it might be logging things when the " +
        "MongoClient is only created.\n18:53:44.815 [cluster-ClusterId{value='603501df75d67c7b989ea382', " +
        "description='null'}-cluster0-shard-00-02.9r3uo.mongodb.net:27017] INFO org.mongodb.driver.cluster - Exception in monitor thread " +
        "while connecting to server cluster0-shard-00-02.9r3uo.mongodb.net:27017\ncom.mongodb.MongoSocketReadException: Prematurely " +
        "reached end of stream").append("tags", asList("documentation", "code")));
    list.add(new Document("description", "Hosts and srvHost are both shown in logs\nEven though only srvHost is used if it exists.\n18:53:43.246 [main] INFO org.mongodb.driver.cluster - Cluster created with settings {hosts=[127.0.0.1:27017], srvHost=cluster0.9r3uo.mongodb.net, mode=MULTIPLE, requiredClusterType=REPLICA_SET, serverSelectionTimeout='30000 ms', requiredReplicaSetName='atlas-oue7si-shard-0'}\n").append("tags", asList("documentation")));
    list.add(new Document("description", "Imports on the driver reactive streams page is wrong Docs ticket for it: https://jira.mongodb.org/browse/DOCS-14248").append("tags", asList("documentation")));
    list.add(new Document("description", "Why are there both BsonObjectId and ObjectId? ObjectId is clearly just a BSON type https://docs.mongodb.com/manual/reference/bson-types/index.html#objectid, but from having both it appears that BsonId is something that exists unrelated to BSON, which does not seem to make sense.").append("tags", asList("documentation")));
    list.add(new Document("description", "new IndexOptions().unique(true) => I'd prefer to have a static IndexOptions.UNIQUE alternative" +
        ".").append("tags", asList("documentation", "sugar", "code")));
    list.add(new Document("description", "MongoCollection.findOneAndReplace does not explain that FindOneAndReplaceOptions.getSort is used in situations when the filter matches multiple documents to pick the one (first) to be replaced. It is also not documented in the server docs. This command is documented for mongo shell, but the shell docs cannot substitute server or driver docs.").append("tags", asList("documentation")));
    list.add(new Document("description", "TransactionBody.execute is allowed to return nulls, so maybe it should be documented with @Nullable. ClientSession.withTransaction is allowed to return nulls, document it with @Nullable?").append("tags", asList("documentation")));
    painPoints.insertMany(list);
  }

  private List<Document> scanCollectionMetadata(MongoClient client) {
    List<Document> databases = client.listDatabases().into(new ArrayList<>());
    List<String> dbs = databases.stream().filter(db -> {
      List<String> admin_dbs = asList("admin", "config", "local", "visualmongodbpro");
      return !admin_dbs.contains(db.getString("name"));
    }).map(doc -> doc.getString("name")).collect(Collectors.toList());

    List<Document> metadata = new ArrayList<>();

    for (String dbString : dbs) {
      MongoDatabase db = client.getDatabase(dbString);

      for (String coll : db.listCollectionNames()) {
        BsonDocument doc = db.getCollection(coll, BsonDocument.class).find().first();
        if (doc == null) break;
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

  DataFetcher<List<String>> getDocumentsByNamespaceDataFetcher() {
    return dataFetchingEnvironment -> {
      try (ClientSession session = client.startSession(clientSessionOptions)) {
        return StreamSupport.stream(client.getDatabase(dataFetchingEnvironment.getArgument("databaseName"))
            .getCollection(dataFetchingEnvironment.getArgument("collectionName"))
            .find(session)
            .sort(Sorts.ascending("_id"))
            .allowDiskUse(true)
            .limit(dataFetchingEnvironment.getArgument("limit"))
            .cursorType(CursorType.NonTailable)
            .spliterator(), false)
            .map(document -> document.toJson(JsonWriterSettings.builder()
                .outputMode(JsonMode.SHELL)
                .indent(false)
                .objectIdConverter((objectId, jsonWriter) -> jsonWriter.writeString(objectId.toHexString()))
                .build()))
            .collect(Collectors.toList());
      }
    };
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

  DataFetcher<ObjectId> replaceDocumentDataFetcher() {
    return dataFetchingEnvironment -> {
      Document document = Document.parse(dataFetchingEnvironment.getArgument("json"));
      ObjectId id = new ObjectId((String) document.get("_id"));
      document.put("_id", id);
      try (ClientSession session = client.startSession(clientSessionOptions)) {
        Document result = session.withTransaction(() -> client.getDatabase(dataFetchingEnvironment.getArgument("databaseName"))
            .getCollection(dataFetchingEnvironment.getArgument("collectionName"))
            .findOneAndReplace(session, Filters.eq("_id", id).toBsonDocument(), document, replaceDocumentOptions));
        if (result == null) {
          throw new RuntimeException(String.format(Locale.ENGLISH, "Did not find a document with _id %s", id));
        }
        return result.getObjectId("_id");
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
