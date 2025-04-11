package com.leisan.rangiffler.test.graphql;

import com.leisan.rangiffler.jupiter.annotation.CreateExtrasUsers;
import com.leisan.rangiffler.jupiter.annotation.CreateUser;
import com.leisan.rangiffler.jupiter.annotation.Extras;
import com.leisan.rangiffler.model.testdata.TestUser;
import com.leisan.rangiffler.gql.FriendsQueryType;
import com.leisan.rangiffler.gql.FriendshipActionType;
import com.leisan.rangiffler.gql.api.BaseApi;
import com.leisan.rangiffler.jupiter.annotation.meta.GqlTest;
import com.leisan.rangiffler.service.impl.AuthApiClient;
import com.leisan.rangiffler.service.impl.AuthClient;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static io.qameta.allure.SeverityLevel.BLOCKER;
import static io.qameta.allure.SeverityLevel.CRITICAL;

@GqlTest
@Tag("graphql")
@Story("Outcome Invitations")
@Feature("Список друзей. API")
public class OutcomeInvitationsTests extends BaseApi {

    @Test
    @Severity(CRITICAL)
    @CreateExtrasUsers({@CreateUser, @CreateUser, @CreateUser})
    @DisplayName("Получение списка исходящих заявок дружбы")
    void outcomeInvitationsListTest(@Extras TestUser[] users, @AuthClient AuthApiClient client1) {
        String extraUserToken1 = client1.doLogin(users[0].getUsername(), users[0].getTestData().password());
        friendSteps.executeAction(extraUserToken1, FriendshipActionType.ADD, users[1].getUsername());
        friendSteps.executeAction(extraUserToken1, FriendshipActionType.ADD, users[2].getUsername());

        Response response = FRIENDS_API.executeFriendsQuery(FriendsQueryType.OUTCOME_INVITATIONS, extraUserToken1, 0, 10, "");
        friendSteps.assertOutcomeInvitationsResponse(
                response,
                Arrays.asList(users[1], users[2]),
                false,
                false,
                Arrays.asList(users[1].getUsername(), users[2].getUsername()),
                "ru",
                "Russian Federation",
                Arrays.asList("INVITATION_SENT", "INVITATION_SENT"));
    }

    @Test
    @Severity(CRITICAL)
    @CreateExtrasUsers({@CreateUser, @CreateUser, @CreateUser})
    @DisplayName("Поиск человека в списке исходящих заявок")
    void outcomeInvitationsSearchTest(@Extras TestUser[] users, @AuthClient AuthApiClient client1) {
        String userName = users[1].getUsername();
        String extraUserToken1 = client1.doLogin(users[0].getUsername(), users[0].getTestData().password());
        friendSteps.executeAction(extraUserToken1, FriendshipActionType.ADD, users[1].getUsername());
        friendSteps.executeAction(extraUserToken1, FriendshipActionType.ADD, users[2].getUsername());

        Response response = FRIENDS_API.executeFriendsQuery(FriendsQueryType.OUTCOME_INVITATIONS, extraUserToken1, 0, 10, userName);
        friendSteps.assertOutcomeInvitationsResponse(
                response,
                Collections.singletonList(users[1]),
                false,
                false,
                Collections.singletonList(userName),
                "ru",
                "Russian Federation",
                Collections.singletonList("INVITATION_SENT"));
    }

    @Test
    @Severity(BLOCKER)
    @CreateExtrasUsers({@CreateUser, @CreateUser})
    @DisplayName("Отправление заявки в друзья")
    void allPeopleAddToFriendListTest(@Extras TestUser[] users, @AuthClient AuthApiClient client1) {
        String extraUserToken1 = client1.doLogin(users[0].getUsername(), users[0].getTestData().password());
        Response response = friendSteps.executeAction(extraUserToken1, FriendshipActionType.ADD, users[1].getUsername());
        friendSteps.assertFriendshipResponse(response, users[1].getUsername(), "INVITATION_SENT");
    }
}