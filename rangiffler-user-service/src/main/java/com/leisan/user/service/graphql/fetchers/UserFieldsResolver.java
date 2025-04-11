package com.leisan.user.service.graphql.fetchers;

import com.leisan.user.service.graphql.dto.CountryDto;
import com.leisan.user.service.graphql.dto.UserConnectionGql;
import com.leisan.user.service.graphql.dto.UserGql;
import com.leisan.user.service.graphql.service.AuthService;
import com.leisan.user.service.graphql.service.CountryService;
import com.leisan.user.service.graphql.service.FriendshipService;
import com.leisan.user.service.graphql.service.UserService;
import com.leisan.user.service.model.FriendStatus;
import graphql.kickstart.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.CompletableFuture;

@Singleton
@RequiredArgsConstructor
public class UserFieldsResolver implements GraphQLResolver<UserGql> {
    private final UserService userService;
    private final FriendshipService friendshipService;
    private final AuthService authService;
    private final CountryService countryService;

    public UserConnectionGql friends(UserGql user,
                                     Integer page,
                                     Integer size,
                                     String searchQuery) {
        return userService.friends(page, size, searchQuery, user.getUsername());
    }

    public UserConnectionGql incomeInvitations(UserGql user,
                                               Integer page,
                                               Integer size,
                                               String searchQuery) {
        return userService.incomeInvitations(user.getId(), searchQuery, page, size);
    }

    public UserConnectionGql outcomeInvitations(UserGql user,
                                                Integer page,
                                                Integer size,
                                                String searchQuery) {
        return userService.outcomeInvitations(user.getId(), searchQuery, page, size);
    }

    public FriendStatus friendStatus(UserGql user,
                                     DataFetchingEnvironment env) {
        return friendshipService.friendStatus(authService.getCurrentUsername(), user.getUsername());
    }

    public CompletableFuture<CountryDto> location(UserGql userGql) {
        var userCountryCode = userService.getUserCountryCode(userGql.getUsername());
        if (StringUtils.isEmpty(userCountryCode)) {
            return CompletableFuture.completedFuture(null);
        } else {
            return countryService.getCountry(userCountryCode).toFuture();
        }
    }

}
