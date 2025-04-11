package com.leisan.photo.service.graphql.fetchers;

import com.leisan.photo.service.graphql.dto.FeedGql;
import com.leisan.photo.service.graphql.service.AuthService;
import graphql.kickstart.tools.GraphQLQueryResolver;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor
public class FeedResolver implements GraphQLQueryResolver {
    private final AuthService authService;

    public FeedGql feed(Boolean withFriends) {
        return new FeedGql(authService.getCurrentUsername(), withFriends, null, null);
    }
}
