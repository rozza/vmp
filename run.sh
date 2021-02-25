#!/bin/bash
trap "./gradlew --stop" EXIT
./gradlew --stop
./gradlew build --continuous --quiet &
./gradlew run
