package com.leisan.photo.service.graphql.dto;

import io.micronaut.serde.annotation.Serdeable;

import java.util.List;

@Serdeable
public record FeedGql(String username,
                      Boolean withFriends,
                      PhotoConnectionGql photos,
                      List<StatGql> stat) {
}
