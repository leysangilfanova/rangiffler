package com.leisan.rangiffler.test.graphql;

import com.leisan.rangiffler.gql.dto.FriendsResponse;
import com.leisan.rangiffler.jupiter.annotation.CreateExtrasUsers;
import com.leisan.rangiffler.jupiter.annotation.CreateUser;
import com.leisan.rangiffler.jupiter.annotation.Extras;
import com.leisan.rangiffler.service.impl.AuthApiClient;
import com.leisan.rangiffler.gql.FriendsQueryType;
import com.leisan.rangiffler.gql.FriendshipActionType;
import com.leisan.rangiffler.gql.api.BaseApi;
import com.leisan.rangiffler.jupiter.annotation.meta.GqlTest;
import com.leisan.rangiffler.model.testdata.TestUser;
import com.leisan.rangiffler.service.impl.AuthClient;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static io.qameta.allure.SeverityLevel.CRITICAL;
import static org.junit.jupiter.api.Assertions.assertEquals;

@GqlTest
@Tag("graphql")
@Story("Друзья")
@Feature("Список друзей. API")
public class FriendsTests extends BaseApi {

    @Test
    @Severity(CRITICAL)
    @CreateExtrasUsers({@CreateUser, @CreateUser, @CreateUser})
    @DisplayName("Получение списка друзей")
    void friendsListTest(@Extras TestUser[] users, @AuthClient AuthApiClient client1, @AuthClient AuthApiClient client2, @AuthClient AuthApiClient client3) {
        String extraUserToken1 = client1.doLogin(users[0].getUsername(), users[0].getTestData().password());
        friendSteps.executeAction(extraUserToken1, FriendshipActionType.ADD, users[1].getUsername());

        String extraUserToken2 = client2.doLogin(users[2].getUsername(), users[2].getTestData().password());
        friendSteps.executeAction(extraUserToken2, FriendshipActionType.ADD, users[1].getUsername());

        String extraUserToken3 = client3.doLogin(users[1].getUsername(), users[1].getTestData().password());
        friendSteps.executeAction(extraUserToken3, FriendshipActionType.ACCEPT, users[0].getUsername());
        friendSteps.executeAction(extraUserToken3, FriendshipActionType.ACCEPT, users[2].getUsername());

        Response response = FRIENDS_API.executeFriendsQuery(FriendsQueryType.FRIENDS, extraUserToken3, 0, 10, "");
        friendSteps.assertFriendsResponse(
                response,
                Arrays.asList(users[0], users[2]),
                false,
                false,
                Arrays.asList(users[0].getUsername(), users[2].getUsername()),
                "ru",
                "Russian Federation",
                Arrays.asList("FRIEND", "FRIEND"));
    }

    @Test
    @Severity(CRITICAL)
    @CreateExtrasUsers({@CreateUser, @CreateUser})
    @DisplayName("Поиск человека в списке друзей")
    void friendsSearchTest(@Extras TestUser[] users, @AuthClient AuthApiClient client1, @AuthClient AuthApiClient client2) {
        String userName = users[0].getUsername();
        String extraUserToken1 = client1.doLogin(users[0].getUsername(), users[0].getTestData().password());
        friendSteps.executeAction(extraUserToken1, FriendshipActionType.ADD, users[1].getUsername());

        String extraUserToken2 = client2.doLogin(users[1].getUsername(), users[1].getTestData().password());
        friendSteps.executeAction(extraUserToken2, FriendshipActionType.ACCEPT, userName);

        Response response = FRIENDS_API.executeFriendsQuery(FriendsQueryType.FRIENDS, extraUserToken2, 0, 10, userName);
        friendSteps.assertFriendsResponse(
                response,
                Collections.singletonList(users[0]),
                false,
                false,
                Collections.singletonList(userName),
                "ru",
                "Russian Federation",
                Collections.singletonList("FRIEND"));
    }

    @Test
    @Severity(CRITICAL)
    @CreateExtrasUsers({@CreateUser, @CreateUser})
    @DisplayName("Удаление друга")
    void deleteFriendTest(@Extras TestUser[] users, @AuthClient AuthApiClient client1, @AuthClient AuthApiClient client2) {
        String userName = users[0].getUsername();
        String extraUserToken1 = client1.doLogin(users[0].getUsername(), users[0].getTestData().password());
        friendSteps.executeAction(extraUserToken1, FriendshipActionType.ADD, users[1].getUsername());

        String extraUserToken2 = client2.doLogin(users[1].getUsername(), users[1].getTestData().password());
        friendSteps.executeAction(extraUserToken2, FriendshipActionType.ACCEPT, userName);

        Response response = friendSteps.executeAction(extraUserToken2, FriendshipActionType.DELETE, userName);
        friendSteps.assertFriendshipResponse(response, userName, null);

        FriendsResponse response2 = FRIENDS_API.executeFriendsQuery(FriendsQueryType.FRIENDS, extraUserToken2, 0, 10, userName).as(FriendsResponse.class);
        assertEquals(response2.getData().getUser().getFriends().getEdges(), new ArrayList<>());
    }
}