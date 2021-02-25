#!/bin/bash
trap "./gradlew --stop" EXIT
./gradlew --stop
./gradlew build -xtest --continuous --quiet &
./gradlew run
