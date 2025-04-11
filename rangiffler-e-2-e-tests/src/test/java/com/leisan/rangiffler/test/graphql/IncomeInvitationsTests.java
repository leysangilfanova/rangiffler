package com.leisan.rangiffler.test.graphql;

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

import java.util.Arrays;
import java.util.Collections;

import static io.qameta.allure.SeverityLevel.BLOCKER;
import static io.qameta.allure.SeverityLevel.CRITICAL;
import static io.qameta.allure.SeverityLevel.NORMAL;

@GqlTest
@Tag("graphql")
@Story("Income Invitations")
@Feature("Список друзей. API")
public class IncomeInvitationsTests extends BaseApi {

    @Test
    @Severity(CRITICAL)
    @CreateExtrasUsers({@CreateUser, @CreateUser, @CreateUser})
    @DisplayName("Получение списка входящих заявок дружбы")
    void incomeInvitationsListTest(@Extras TestUser[] users, @AuthClient AuthApiClient client1, @AuthClient AuthApiClient client2, @AuthClient AuthApiClient client3) {
        String extraUserToken1 = client1.doLogin(users[0].getUsername(), users[0].getTestData().password());
        friendSteps.executeAction(extraUserToken1, FriendshipActionType.ADD, users[1].getUsername());

        String extraUserToken2 = client2.doLogin(users[2].getUsername(), users[2].getTestData().password());
        friendSteps.executeAction(extraUserToken2, FriendshipActionType.ADD, users[1].getUsername());

        String extraUserToken3 = client3.doLogin(users[1].getUsername(), users[1].getTestData().password());
        Response response = FRIENDS_API.executeFriendsQuery(FriendsQueryType.INCOME_INVITATIONS, extraUserToken3, 0, 10, "");
        friendSteps.assertIncomeInvitationsResponse(
                response,
                Arrays.asList(users[0], users[1]),
                false,
                false,
                Arrays.asList(users[0].getUsername(), users[2].getUsername()),
                "ru",
                "Russian Federation",
                Arrays.asList("INVITATION_RECEIVED", "INVITATION_RECEIVED"));
    }

    @Test
    @Severity(CRITICAL)
    @CreateExtrasUsers({@CreateUser, @CreateUser, @CreateUser})
    @DisplayName("Поиск человека в списке входящих заявок")
    void incomeInvitationsSearchTest(@Extras TestUser[] users, @AuthClient AuthApiClient client1, @AuthClient AuthApiClient client2, @AuthClient AuthApiClient client3) {
        String userName = users[0].getUsername();

        String extraUserToken1 = client1.doLogin(userName, users[0].getTestData().password());
        friendSteps.executeAction(extraUserToken1, FriendshipActionType.ADD, users[1].getUsername());

        String extraUserToken2 = client2.doLogin(users[2].getUsername(), users[2].getTestData().password());
        friendSteps.executeAction(extraUserToken2, FriendshipActionType.ADD, users[1].getUsername());

        String extraUserToken3 = client3.doLogin(users[1].getUsername(), users[1].getTestData().password());

        Response response = FRIENDS_API.executeFriendsQuery(FriendsQueryType.INCOME_INVITATIONS, extraUserToken3, 0, 10, userName);
        friendSteps.assertIncomeInvitationsResponse(
                response,
                Collections.singletonList(users[0]),
                false,
                false,
                Collections.singletonList(userName),
                "ru",
                "Russian Federation",
                Collections.singletonList("INVITATION_RECEIVED"));
    }

    @Test
    @Severity(BLOCKER)
    @CreateExtrasUsers({@CreateUser, @CreateUser})
    @DisplayName("Прием заявки в друзья")
    void acceptInvitationToFriendsTest(@Extras TestUser[] users, @AuthClient AuthApiClient client1, @AuthClient AuthApiClient client2) {
        String userName = users[0].getUsername();
        String extraUserToken1 = client1.doLogin(userName, users[0].getTestData().password());
        friendSteps.executeAction(extraUserToken1, FriendshipActionType.ADD, users[1].getUsername());

        String extraUserToken2 = client2.doLogin(users[1].getUsername(), users[1].getTestData().password());

        Response response = friendSteps.executeAction(extraUserToken2, FriendshipActionType.ACCEPT, userName);
        friendSteps.assertFriendshipResponse(response, userName, "FRIEND");
    }

    @Test
    @Severity(NORMAL)
    @CreateExtrasUsers({@CreateUser, @CreateUser})
    @DisplayName("Отклонение заявки в друзья")
    void declineInvitationToFriendsTest(@Extras TestUser[] users, @AuthClient AuthApiClient client1, @AuthClient AuthApiClient client2) {
        String userName = users[0].getUsername();
        String extraUserToken1 = client1.doLogin(userName, users[0].getTestData().password());
        friendSteps.executeAction(extraUserToken1, FriendshipActionType.ADD, users[1].getUsername());

        String extraUserToken2 = client2.doLogin(users[1].getUsername(), users[1].getTestData().password());

        Response response = friendSteps.executeAction(extraUserToken2, FriendshipActionType.REJECT, userName);
        friendSteps.assertFriendshipResponse(response, userName, null);
    }
}