plugins {
    `java-library`
}

dependencies {
    implementation("com.jayway.jsonpath:json-path:2.9.0")
    implementation("org.assertj:assertj-core:3.27.3")
}

java {
    sourceCompatibility = JavaVersion.toVersion("21")
    targetCompatibility = JavaVersion.toVersion("21")
}