package com.leisan.photo.service.graphql.dto;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record StatGql(Integer count, CountryDto country) {
}
