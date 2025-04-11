package com.leisan.user.service.graphql.service;

import com.leisan.user.service.graphql.dto.CountryDto;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Singleton
@RequiredArgsConstructor
public class CountryService {
    @Inject
    @Client("http://country-service:4001/internal/country")
    private HttpClient httpClient;

    public Mono<Boolean> countryExists(String countryCode) {
        return Mono.from(httpClient.retrieve(
                HttpRequest.GET("/" + countryCode)
                        .contentType("application/json")
                        .accept(MediaType.APPLICATION_JSON),
                Argument.of(CountryDto.class)
        )).map(country -> country.code().equals(countryCode));
    }

    public Mono<CountryDto> getCountry(String countryCode) {
        return Mono.from(httpClient.retrieve(
                HttpRequest.GET("/" + countryCode)
                        .contentType("application/json")
                        .accept(MediaType.APPLICATION_JSON),
                Argument.of(CountryDto.class)
        ));
    }
}
