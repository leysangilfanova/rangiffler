package com.leisan.photo.service.graphql.dto;

import io.micronaut.serde.annotation.Serdeable;

import java.time.Instant;

@Serdeable
public record PhotoGql(String id,
                       String src,
                       String description,
                       String countryCode,
                       Instant creationDate) {
}
