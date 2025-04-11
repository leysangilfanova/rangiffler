package com.leisan.photo.service.graphql.service;

import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import reactor.core.publisher.Mono;

import java.util.List;

@Singleton
@RequiredArgsConstructor
public class UserService {
    @Inject
    @Client("http://user-service:4002/internal/users")
    private HttpClient httpClient;

    @SneakyThrows
    public Mono<List<String>> getFriends(String username) {
        return Mono.from(httpClient.retrieve(
                HttpRequest.GET("/" + username + "/friends"),
                Argument.listOf(String.class)
        ));
    }
}
