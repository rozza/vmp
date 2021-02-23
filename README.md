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

#### Why does my code reformat?

Uses [spotless](https://github.com/diffplug/spotless/tree/main/plugin-gradle) for auto linting of code.
See the spotless config in the build.gradle.kts

Git pre-commit hook to run spotless can be found in ./config/git-pre-commit
`ln -s $(pwd)/config/git-pre-commit ./.git/hooks/pre-commit`
