#!/bin/bash

./gradlew sonarqube \
  -Dsonar.projectKey=healthy-future-backend \
  -Dsonar.host.url=${SONAR_HOST_URL} \
  -Dsonar.login=${SONAR_LOGIN} \
  -Dsonar.coverage.jacoco.xmlReportPaths=build/reports/jacoco/test/jacocoTestReport.xml
