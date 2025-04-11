package com.leisan.country.service.graphql.dto;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record CountryGql(String flag,
                         String code,
                         String name) {
}
