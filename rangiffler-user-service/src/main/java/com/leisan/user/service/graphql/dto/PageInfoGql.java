package com.leisan.user.service.graphql.dto;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record PageInfoGql(Boolean hasPreviousPage,
                          Boolean hasNextPage) {
}
