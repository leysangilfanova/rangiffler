package com.leisan.user.service.graphql.fetchers;

import com.leisan.user.service.graphql.dto.FriendshipInputGql;
import com.leisan.user.service.graphql.dto.UserGql;
import com.leisan.user.service.graphql.dto.UserInputGql;
import com.leisan.user.service.graphql.service.AuthService;
import com.leisan.user.service.graphql.service.FriendshipService;
import com.leisan.user.service.graphql.service.UserService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.schema.DataFetchingEnvironment;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor
public class UserMutationResolver implements GraphQLMutationResolver {

    private final UserService userService;
    private final FriendshipService friendshipService;
    private final AuthService authService;

    public UserGql user(UserInputGql userInputGql,
                        DataFetchingEnvironment env) {
        return userService.updateUser(authService.getCurrentUsername(), userInputGql);
    }

    public UserGql friendship(FriendshipInputGql friendshipInputGql,
                              DataFetchingEnvironment env) {
        var username = authService.getCurrentUsername();
        var targetUser = userService.getUser(friendshipInputGql.user());
        switch (friendshipInputGql.action()) {
            case ADD -> friendshipService.createFriendshipRequest(username, targetUser.getUsername());
            case ACCEPT -> friendshipService.acceptFriendshipRequest(username, targetUser.getUsername());
            case REJECT -> friendshipService.declineFriendshipRequest(username, targetUser.getUsername());
            case DELETE -> friendshipService.removeFriend(username, targetUser.getUsername());
            default -> throw new IllegalArgumentException("Unsupported action: " + friendshipInputGql.action());
        }
        return userService.getUser(friendshipInputGql.user());
    }
}
