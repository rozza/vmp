package org.mongo.visualmongopro.rest;

import org.bson.Document;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocRepository {
  List<Document> getAllJsonSchemas();

  List<Document> getAllJsonSchemasByDatabase(String db);

  Document getAllJsonSchemasByDatabaseAndCollection(String db, String coll);

  List<Document> getDocs(String db, String coll, int skip, int limit);
}
