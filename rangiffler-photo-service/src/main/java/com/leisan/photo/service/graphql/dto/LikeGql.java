package com.leisan.photo.service.graphql.dto;

import io.micronaut.serde.annotation.Serdeable;

import java.time.Instant;

@Serdeable
public record LikeGql(String user,
                      String username,
                      Instant creationDate) {
}
