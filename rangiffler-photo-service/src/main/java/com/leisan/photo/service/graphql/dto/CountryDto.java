package com.leisan.photo.service.graphql.dto;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record CountryDto(String flag,
                         String code,
                         String name) {
}
