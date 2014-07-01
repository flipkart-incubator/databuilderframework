#!/usr/bin/env bash
mvn clean source:jar deploy -DskipTests -DaltDeploymentRepository=flipkart::default::http://artifactory.nm.flipkart.com:8081/artifactory/libs-release-local
mvn clean source:jar deploy -DskipTests -DaltDeploymentRepository=flipkart::default::http://artifactory.nm.flipkart.com:8081/artifactory/libs-releases-local