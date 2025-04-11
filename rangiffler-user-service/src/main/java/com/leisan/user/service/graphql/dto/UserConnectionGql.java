package com.leisan.user.service.graphql.dto;

import io.micronaut.serde.annotation.Serdeable;

import java.util.List;

@Serdeable
public record UserConnectionGql(Integer total, List<UserEdgeGql> edges, PageInfoGql pageInfo) {
}
