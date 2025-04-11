package com.leisan.photo.service.graphql.dto;

import io.micronaut.serde.annotation.Serdeable;

import java.util.List;

@Serdeable
public record LikesGql(Integer total, List<LikeGql> likes) {
}
