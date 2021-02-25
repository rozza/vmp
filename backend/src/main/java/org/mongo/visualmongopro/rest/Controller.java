package org.mongo.visualmongopro.rest;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class Controller {

  private final static Logger LOGGER = LoggerFactory.getLogger(Controller.class);
  private final DocRepository docRepository;

  public Controller(DocRepository docRepository) {
    this.docRepository = docRepository;
  }

  @GetMapping("json-schemas")
  @ResponseStatus(HttpStatus.OK)
  public List<Document> getJsonSchemas() {
    return docRepository.getAllJsonSchemas();
  }

  @GetMapping("json-schemas/{db}")
  @ResponseStatus(HttpStatus.OK)
  public List<Document> getJsonSchemasByDatabase(@PathVariable String db) {
    return docRepository.getAllJsonSchemasByDatabase(db);
  }

  @GetMapping("json-schemas/{db}/{coll}")
  @ResponseStatus(HttpStatus.OK)
  public Document getJsonSchemasByDatabaseAndCollection(@PathVariable String db, @PathVariable String coll) {
    return docRepository.getAllJsonSchemasByDatabaseAndCollection(db, coll);
  }

  @GetMapping("docs/{db}/{coll}/{skip}/{limit}")
  @ResponseStatus(HttpStatus.OK)
  public List<Document> getDocs(@PathVariable String db, @PathVariable String coll, @PathVariable int skip, @PathVariable int limit) {
    return docRepository.getDocs(db, coll, skip, limit);
  }

  @GetMapping("mdb/dbs")
  @ResponseStatus(HttpStatus.OK)
  public List<String> getDBs() {
    return docRepository.getAllDatabases();
  }

  @GetMapping("mdb/collections/{db}")
  @ResponseStatus(HttpStatus.OK)
  public List<String> getCollections(@PathVariable String db) {
    return docRepository.getAllCollections(db);
  }

  @ExceptionHandler(RuntimeException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public final Exception handleAllExceptions(RuntimeException e) {
    LOGGER.error("Internal server error.", e);
    return e;
  }
}
