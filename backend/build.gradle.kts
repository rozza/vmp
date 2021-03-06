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

import java.util.Properties;

plugins {
  // Apply the java plugin to add support for Java
  java

  kotlin("jvm") version "1.4.30"

  id("org.springframework.boot") version "2.4.3"
  id("io.spring.dependency-management") version "1.0.11.RELEASE"
}

java {
  toolchain {
    languageVersion.set(JavaLanguageVersion.of(14))
  }
}

tasks.withType<JavaExec> {
  // Support local properties
  val properties = Properties()
  if (rootProject.file("local.properties").exists()) {
    properties.load(rootProject.file("local.properties").inputStream())
  }

  for (property in properties) {
    systemProperty(property.key.toString(), property.value)
  }

  for (property in properties) {
    environment(property.key.toString(), property.value)
  }

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
  implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
  implementation("io.springfox:springfox-boot-starter:3.0.0")

  runtimeOnly("org.springframework.boot:spring-boot-devtools")
  runtimeOnly("com.graphql-java-kickstart:graphiql-spring-boot-starter:7.0.1")

  testImplementation("org.springframework.boot:spring-boot-starter-test")

  // Use JUnit Jupiter API for testing.
  testImplementation("org.junit.jupiter:junit-jupiter-api:5.4.2")
  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.4.2")
}


val test by tasks.getting(Test::class) {
  // Use junit platform for unit tests
  useJUnitPlatform()
}

