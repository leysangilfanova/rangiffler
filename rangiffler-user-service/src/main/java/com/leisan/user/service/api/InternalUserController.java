package com.leisan.user.service.api;

import com.leisan.user.service.graphql.dto.UserGql;
import com.leisan.user.service.graphql.service.UserService;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Controller(value = "/internal/users")
@RequiredArgsConstructor
public class InternalUserController {
    private final UserService userService;

    @Get(value = "/{username}/friends")
    public List<String> friends(@PathVariable(value = "username") String username) {
        return userService.userFriends(username).stream()
                .map(UserGql::getUsername)
                .toList();
    }
}
