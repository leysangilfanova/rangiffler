#!/bin/bash
./gradlew :rangiffler-graphql-common:build :rangiffler-photo-service:build :rangiffler-user-service:build :rangiffler-country-service:build :rangiffler-auth:build
docker compose build
docker compose up