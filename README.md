# Visual MongoDB Pro

Its a **Pro**totype with a very loosely defined scope.


## To run it
```
./gradlew run
```

http://localhost:8080

Can use Java or Kotlin code.

The main routes are configured by the spring framework

## Structure

 * frontend - the react frontend
 * backend - the spring framework backend

Currently, the react frontend merges into backend/resources/static because its easier than running two web servers.

### Continuous development

 * Use the handy shell script: `./run.sh`
 * To build the react stuff: `./gradlew frontendBuild`

### GraphQL

The graphql entry point is at the URI path `/graphql` The GraphQL schema is in `src/main/resources/schema.graphqls`.

The best way to interact with the GraphQL data services is with a dedicated HTTP client that is GraphQL-aware.

Three possibilities are:

* http://localhost:8080/graphiql - via the [graphiql library](https://github.com/graphql/graphiql/blob/main/packages/graphiql/README.md)
* https://github.com/graphql/graphql-playground.  I (Jeff) had a bit of trouble figuring out how to run the desktop version of this on OS X
  after installation via brew, but it's possible.
* https://www.postman.com/product/rest-client/.  This is the one that Maxime uses.

The MongoDB connection string defaults to mongodb://localhost:27017 but can be overridden via the `MONGODB_URI` environment variable.
See the `MongoClientAppConfig` class if you want to see how that's done or play around with alternatives.

#### Create a document
```shell
$ curl --location --request POST 'localhost:8080/graphql' \
--header 'Content-Type: application/json' \
--data-raw '{"query":"{createDocument(databaseName:\"vakoDB\", collectionName:\"vakoCollection\", json:\"{\\\"fieldName\\\":\\\"fieldValue\\\"}\")}","operationName":"","variables":{}}'
```
Response data:
```json
{"data":{"createDocument":"6036eaafb1c773636b6e80f5"}}
```

### REST API

#### Get all the JSON Schemas

```shell
curl --location --request GET 'localhost:8080/api/json-schemas/'
```

Returns `List<Document>`.

#### Get all the JSON Schemas for a specific DB

```shell
curl --location --request GET 'localhost:8080/api/json-schemas/sample_mflix'
```

Returns `List<Document>`.

#### Get all the JSON Schemas for a specific DB & collection

```shell
curl --location --request GET 'localhost:8080/api/json-schemas/sample_mflix/movies'
```

Returns `Document`.

#### Get docs

Where 0 is the skip and 10 the limit (sort by _id by default)

```shell
curl --location --request GET 'http://localhost:8080/api/docs/sample_mflix/movies/0/10'
```

Returns `List<Document>`.

### Swagger 3

- Swagger 3 is already configured in this project in `SpringFoxConfig.java`.
- The Swagger UI can be seen at [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html).
- The Swagger API documentation 2.0 is at [http://localhost:8080/v2/api-docs](http://localhost:8080/v2/api-docs).
- The Open API documentation 3.0.3 is at [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs).
- You can also try the entire REST API directly from the Swagger interface!

## Why does my code reformat?

Uses [spotless](https://github.com/diffplug/spotless/tree/main/plugin-gradle) for auto linting of code.
See the spotless config in the build.gradle.kts

Git pre-commit hook to run spotless can be found in ./config/git-pre-commit
`ln -s $(pwd)/config/git-pre-commit ./.git/hooks/pre-commit`
