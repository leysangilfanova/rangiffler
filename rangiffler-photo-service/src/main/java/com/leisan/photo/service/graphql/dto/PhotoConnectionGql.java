package com.leisan.photo.service.graphql.dto;

import io.micronaut.serde.annotation.Serdeable;

import java.util.List;

@Serdeable
public record PhotoConnectionGql(Integer total,
                                 List<PhotoEdge> edges,
                                 PageInfoGql pageInfo) {
}
