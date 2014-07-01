#!/usr/bin/env bash
mvn clean source:jar deploy -DskipTests -DaltDeploymentRepository=flipkart::default::http://artifactory.nm.flipkart.com:8081/artifactory/libs-snapshot-local
mvn clean source:jar deploy -DskipTests -DaltDeploymentRepository=flipkart::default::http://artifactory.nm.flipkart.com:8081/artifactory/libs-snapshots-local