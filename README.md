# Viz MongoDB Pro

Its a **Pro**totype with a loosely defined scope.


./gradlew run

http://localhost:4567/hello


---

Can use Java or Kotlin code.

The main routes are configured by:

```
application {
  mainClass.set("org.mongo.visualmongopro.KMainKt")
}
```


---
Why does my code reformat?

Uses [spotless](https://github.com/diffplug/spotless/tree/main/plugin-gradle) for auto linting of code.
See the spotless config in the build.gradle.kts

Git pre-commit hook to run spotless can be found in ./config/git-pre-commit
`ln -s $(pwd)/config/git-pre-commit ./.git/hooks/pre-commit`
