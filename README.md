# Visual MongoDB Pro

Its a **Pro**totype with a very loosely defined scope.

```
./gradlew run
```

http://localhost:8080


---

Can use Java or Kotlin code.

The main routes are configured by the spring framework


---
Why does my code reformat?

Uses [spotless](https://github.com/diffplug/spotless/tree/main/plugin-gradle) for auto linting of code.
See the spotless config in the build.gradle.kts

Git pre-commit hook to run spotless can be found in ./config/git-pre-commit
`ln -s $(pwd)/config/git-pre-commit ./.git/hooks/pre-commit`
