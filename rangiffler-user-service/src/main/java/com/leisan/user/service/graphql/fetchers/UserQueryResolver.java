package com.leisan.user.service.graphql.fetchers;

import com.leisan.user.service.graphql.dto.UserConnectionGql;
import com.leisan.user.service.graphql.dto.UserGql;
import com.leisan.user.service.graphql.service.AuthService;
import com.leisan.user.service.graphql.service.UserService;
import graphql.kickstart.tools.GraphQLQueryResolver;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor
public class UserQueryResolver implements GraphQLQueryResolver {
    private final UserService userService;
    private final AuthService authService;

    public UserGql user() {
        return userService.getUserByUsername(authService.getCurrentUsername());
    }

    public UserConnectionGql users(Integer page,
                                   Integer size,
                                   String searchQuery) {
        return userService.getUsers(page, size, searchQuery);
    }

}
