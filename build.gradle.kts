/*
 * Copyright 2008-present MongoDB, Inc.
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
 */

plugins {
  // Apply the java plugin to add support for Java
  java

  kotlin("jvm") version "1.4.30"

  id("com.diffplug.spotless") version "5.10.0"

  id("org.springframework.boot") version "2.4.3"
  id("io.spring.dependency-management") version "1.0.11.RELEASE"
  id("cool.william.frontend") version "0.0.15"
}

tasks.processResources {
  dependsOn("frontendBuild")
}

repositories {
  // Use jcenter for resolving dependencies.
  // You can declare any Maven/Ivy/file repository here.
  jcenter()
}

dependencies {
  implementation("org.mongodb:mongodb-driver-sync:4.2.1")
  implementation("org.mongodb:mongodb-driver-core:4.2.1")
  implementation("org.mongodb:bson:4.2.1")

  implementation("com.graphql-java:graphql-java:16.2")
  implementation("com.graphql-java:graphql-java-extended-scalars:16.0.0")
  implementation("com.graphql-java:graphql-java-spring-boot-starter-webmvc:2.0")
  implementation("com.google.guava:guava:26.0-jre")
  implementation("org.springframework.boot:spring-boot-starter-web")

  testImplementation("org.springframework.boot:spring-boot-starter-test")

  // Use JUnit Jupiter API for testing.
  testImplementation("org.junit.jupiter:junit-jupiter-api:5.4.2")
  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.4.2")
}

val test by tasks.getting(Test::class) {
  // Use junit platform for unit tests
  useJUnitPlatform()
}

// Spotless is used to lint and reformat source files.
spotless {
  java {
    googleJavaFormat()
    importOrder("java", "io", "org", "org.bson", "com.mongodb", "")
    removeUnusedImports() // removes any unused imports
    trimTrailingWhitespace()
    endWithNewline()
    indentWithSpaces()
  }

  kotlin {
    ktlint("0.37.2").userData(mapOf("indent_size" to "2", "continuation_indent_size" to "2"))
    trimTrailingWhitespace()
    indentWithSpaces()
    endWithNewline()
  }

  kotlinGradle {
    ktlint("0.37.2").userData(mapOf("indent_size" to "2", "continuation_indent_size" to "2"))
    trimTrailingWhitespace()
    indentWithSpaces()
    endWithNewline()
  }

  format("extraneous") {
    target("*.xml", "*.yml", "*.md")
    trimTrailingWhitespace()
    indentWithSpaces()
    endWithNewline()
  }
}

tasks.register("run") {
  dependsOn("bootRun")
}
