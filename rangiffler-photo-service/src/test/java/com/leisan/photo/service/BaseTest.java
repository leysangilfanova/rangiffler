package com.leisan.photo.service;

import com.leisan.photo.service.graphql.dto.CountryDto;
import com.leisan.photo.service.graphql.service.AuthService;
import com.leisan.photo.service.graphql.service.CountryService;
import com.leisan.photo.service.graphql.service.UserService;
import com.leisan.photo.service.repository.LikeRepository;
import com.leisan.photo.service.repository.PhotoRepository;
import io.micronaut.context.annotation.Replaces;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.runtime.EmbeddedApplication;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.utils.SecurityService;
import io.micronaut.serde.ObjectMapper;
import io.micronaut.test.annotation.MockBean;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@MicronautTest(environments = "test")
public abstract class BaseTest {

    @Inject
    EmbeddedApplication<?> application;
    @Inject
    @Client("/")
    HttpClient httpClient;
    @Inject
    ObjectMapper objectMapper;
    @Inject
    PhotoRepository photoRepository;
    @Inject
    LikeRepository likeRepository;
    @Inject
    CountryService countryService;
    @Inject
    AuthService authService;
    @Inject
    UserService userService;
    String currentUsername = "test";

    GraphqlClient graphqlClient;

    @BeforeEach
    void init() {
        graphqlClient = new GraphqlClient(httpClient, objectMapper);
    }

    @AfterEach
    void tearDown() {
        likeRepository.deleteAll();
        photoRepository.deleteAll();
    }

    @Test
    void testItWorks() {
        Assertions.assertTrue(application.isRunning());
    }


    @MockBean(CountryService.class)
    @Replaces(CountryService.class)
    @Singleton
    CountryService mockCountryService() {
        return new CountryService() {
            @Override
            public Mono<Boolean> countryExists(String countryCode) {
                return Mono.just(true);
            }

            @Override
            public Mono<CountryDto> getCountry(String countryCode) {
                return Mono.just(new CountryDto("test", countryCode, countryCode));
            }
        };

    }

    @MockBean(UserService.class)
    @Replaces(UserService.class)
    @Singleton
    UserService mockUserService() {
        return new UserService() {
            @Override
            public Mono<List<String>> getFriends(String username) {
                return Mono.fromSupplier(List::of);
            }
        };
    }

    @MockBean(AuthService.class)
    @Replaces(AuthService.class)
    @Singleton
    AuthService mockAuthService() {
        return new AuthService(new SecurityService() {
            @Override
            public Optional<String> username() {
                return Optional.empty();
            }

            @Override
            public Optional<Authentication> getAuthentication() {
                return Optional.empty();
            }

            @Override
            public boolean isAuthenticated() {
                return false;
            }

            @Override
            public boolean hasRole(String role) {
                return false;
            }
        }) {
            @Override
            public String getCurrentUsername() {
                return currentUsername;
            }
        };
    }
}
