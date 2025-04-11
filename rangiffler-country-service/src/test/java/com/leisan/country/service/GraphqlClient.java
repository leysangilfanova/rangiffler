package com.leisan.country.service;

import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.HttpClient;
import io.micronaut.json.tree.JsonNode;
import io.micronaut.serde.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@RequiredArgsConstructor
public class GraphqlClient {
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    @SneakyThrows
    public String makeGraphqlCall(GraphqlRq graphqlRq) {
        HttpRequest<?> request = HttpRequest.POST("/graphql", graphqlRq)
                .contentType("application/json")
                .accept(MediaType.APPLICATION_GRAPHQL)
                .accept(MediaType.APPLICATION_JSON);
        var rs = httpClient.toBlocking().retrieve(request, Argument.of(JsonNode.class));
        return objectMapper.writeValueAsString(rs);
    }
}
