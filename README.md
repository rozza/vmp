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

### Continuous build

 * Start Gradle build as a continuous task: `./gradlew build --continuous`
 * Start Gradle bootRun task: ./gradlew run`
 * Start build the react stuff: `./gradlew frontendBuild` - it will reload

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

#### Why does my code reformat?

Uses [spotless](https://github.com/diffplug/spotless/tree/main/plugin-gradle) for auto linting of code.
See the spotless config in the build.gradle.kts

Git pre-commit hook to run spotless can be found in ./config/git-pre-commit
`ln -s $(pwd)/config/git-pre-commit ./.git/hooks/pre-commit`
