#!/bin/bash

set -e -u
./gradlew clean build
./gradlew jacocoTestReport