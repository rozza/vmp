#!/bin/bash
./gradlew --stop
./gradlew build --continuous --quiet &
./gradlew run
