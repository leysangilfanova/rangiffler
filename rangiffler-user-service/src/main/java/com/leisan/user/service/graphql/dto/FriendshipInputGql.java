package com.leisan.user.service.graphql.dto;

import com.leisan.user.service.model.FriendshipAction;
import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record FriendshipInputGql(String user,
                                 FriendshipAction action) {
}
