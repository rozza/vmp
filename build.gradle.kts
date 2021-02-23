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

buildscript {
  repositories {
    jcenter()
  }
}

tasks.register("run") {
  dependsOn(":backend:bootRun")
}

allprojects {
  repositories {
    // Use jcenter for resolving dependencies.
    // You can declare any Maven/Ivy/file repository here.
    jcenter()
  }
}

plugins {
  java
  id("com.diffplug.spotless") version "5.10.0"
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
