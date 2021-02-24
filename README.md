# Viz MongoDB Pro

Its a **Pro**totype with a loosely defined scope.


./gradlew run

This branch uses a Java implementation of GraphQL and is based off a tutorial at
https://www.graphql-java.com/tutorials/getting-started-with-spring-boot/ that has been modified to use MongoDB.

As it's Spring Boot, the default is to start up a server on http://localhost:8080, though of course this can be changed.

The graphql entry point is at the URI path `/graphql`

The best way to interact with the GraphQL data services is with a dedicated HTTP client that is GraphQL aware.  Two possibilities are:

* https://github.com/graphql/graphql-playground.  I (Jeff) had a bit of trouble figuring out how to run the desktop version of this on OS X
  after
  installation via brew, but it's possible.
* https://www.postman.com/product/rest-client/.  This is the one that Maxime uses.

The MongoDB connection string defaults to mongodb://localhost:27017 but can be overridden via the `MONGODB_URI` environment variable.
See the `MongoClientAppConfig` class if you want to see how that's done or play around with alternatives.

## Create a book via HTTP 
`POST http://localhost:8080/graphql` with the header `Content-Type: application/json` and the raw body

```json
{"query":"{createBook(book:{name:\"Nonsense\", publicationDate:\"2021-02-24T12:23+00:00\", price:\"23.5\", pageCount: 230})}", "operationName":"","variables":{}}
```

The `createBook` command is defined in `src/main/resources/schema.graphqls`
and is mapped to the corresponding `DataFetcher` (it persists a new book) in `GraphQLProvider.buildWiring`.

Example:
```bash
$ curl --location --request POST 'localhost:8080/graphql' \
--header 'Content-Type: application/json' \
--data-raw '{"query":"{createBook(book:{name:\"Nonsense\", publicationDate:\"2021-02-24T12:23+00:00\", price:\"23.5\", pageCount: 230})}", "operationName":"","variables":{}}'
```
Response data:
```json
{"data":{"createBook":"BsonObjectId{value=6036ab32ef266b3e3137afe8}"}}
```

---
Why does my code reformat?

Uses [spotless](https://github.com/diffplug/spotless/tree/main/plugin-gradle) for auto linting of code.
See the spotless config in the build.gradle.kts

Git pre-commit hook to run spotless can be found in ./config/git-pre-commit
`ln -s $(pwd)/config/git-pre-commit ./.git/hooks/pre-commit`
