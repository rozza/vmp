#!/bin/bash
trap "./gradlew --stop" EXIT
./gradlew --stop
./gradlew clean
./gradlew build -xtest --continuous --quiet &
./gradlew run
