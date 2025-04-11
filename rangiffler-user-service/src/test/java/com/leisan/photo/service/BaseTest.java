package com.leisan.photo.service;

import com.leisan.user.service.graphql.dto.CountryDto;
import com.leisan.user.service.graphql.service.AuthService;
import com.leisan.user.service.graphql.service.CountryService;
import com.leisan.user.service.graphql.service.FriendshipService;
import com.leisan.user.service.graphql.service.UserService;
import com.leisan.user.service.kafka.UserListener;
import com.leisan.user.service.repository.FriendshipRepository;
import com.leisan.user.service.repository.UserRepository;
import io.micronaut.context.annotation.Replaces;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.runtime.EmbeddedApplication;
import io.micronaut.serde.ObjectMapper;
import io.micronaut.test.annotation.MockBean;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;

@MicronautTest(environments = "test", transactional = false)
public abstract class BaseTest {

    @Inject
    EmbeddedApplication<?> application;
    @Inject
    @Client("/")
    HttpClient httpClient;
    @Inject
    ObjectMapper objectMapper;
    @Inject
    CountryService countryService;
    @Inject
    AuthService authService;
    @Inject
    UserListener userListener;
    @Inject
    UserRepository userRepository;
    @Inject
    FriendshipRepository friendshipRepository;
    @Inject
    UserService userService;
    @Inject
    FriendshipService friendshipService;

    GraphqlClient graphqlClient;

    @BeforeEach
    void init() {
        graphqlClient = new GraphqlClient(httpClient, objectMapper);
    }

    @AfterEach
    void tearDown() {
        friendshipRepository.deleteAll();
        userRepository.deleteAll();

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

    @MockBean(AuthService.class)
    @Replaces(AuthService.class)
    @Singleton
    AuthService mockAuthService() {
        return Mockito.mock(AuthService.class);
    }

}
