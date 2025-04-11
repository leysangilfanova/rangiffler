#!/bin/bash
./gradlew :rangiffler-e-2-e-tests:build
cd rangiffler-e-2-e-tests
allure generate build/allure-results --clean -o build/allure-report
allure open build/allure-report