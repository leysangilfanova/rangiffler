package com.leisan.user.service.graphql.dto;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record UserInputGql(String firstname,
                           String surname,
                           String avatar,
                           CountryInputGql location) {
}
