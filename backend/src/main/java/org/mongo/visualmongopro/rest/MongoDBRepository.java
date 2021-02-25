package org.mongo.visualmongopro.rest;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Sorts.ascending;

@Repository
public class MongoDBRepository implements DocRepository {

  private final MongoClient client;
  private MongoCollection<Document> jsonSchemaCollection;

  public MongoDBRepository(MongoClient mongoClient) {
    this.client = mongoClient;
  }

  @PostConstruct
  void init() {
    jsonSchemaCollection = client.getDatabase("visualmongodbpro").getCollection("collection.metadata");
  }


  @Override
  public List<Document> getAllJsonSchemas() {
    return jsonSchemaCollection.find().into(new ArrayList<>());
  }

  @Override
  public List<Document> getAllJsonSchemasByDatabase(final String db) {
    return jsonSchemaCollection.find(eq("databaseName", db)).into(new ArrayList<>());
  }

  @Override
  public Document getAllJsonSchemasByDatabaseAndCollection(final String db, final String coll) {
    return jsonSchemaCollection.find(and(eq("databaseName", db), eq("collectionName", coll))).first();
  }

  @Override
  public List<Document> getDocs(final String db, final String coll, final int skip, final int limit) {
    return client.getDatabase(db).getCollection(coll).find().sort(ascending("_id")).skip(skip).limit(limit).into(new ArrayList<>());
  }

  @Override
  public List<String> getAllDatabases() {
    List<String> admin_dbs = Arrays.asList("admin", "config", "local", "visualmongodbpro");
    return client.listDatabases().map(db -> db.getString("name")).into(new ArrayList<>()).stream().filter(s -> !admin_dbs.contains(s)).collect(Collectors.toList());
  }

  @Override
  public List<String> getAllCollections(final String db) {
    return client.getDatabase(db).listCollectionNames().into(new ArrayList<>());
  }
}
