package com.leisan.user.service.graphql.dto;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record UserEdgeGql(UserGql node) {
}
