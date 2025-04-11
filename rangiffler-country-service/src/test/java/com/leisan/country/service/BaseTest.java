package com.leisan.country.service;

import io.micronaut.context.annotation.Property;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.runtime.EmbeddedApplication;
import io.micronaut.serde.ObjectMapper;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@MicronautTest(environments = "test")
public abstract class BaseTest {

    @Inject
    EmbeddedApplication<?> application;
    @Inject
    @Client("/")
    HttpClient httpClient;
    @Inject
    ObjectMapper objectMapper;

    GraphqlClient graphqlClient;

    @BeforeEach
    void init() {
        graphqlClient = new GraphqlClient(httpClient, objectMapper);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testItWorks() {
        Assertions.assertTrue(application.isRunning());
    }

}
