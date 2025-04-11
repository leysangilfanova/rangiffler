package com.leisan.rangiffler.test.graphql;

import com.leisan.rangiffler.jupiter.annotation.CreateExtrasUsers;
import com.leisan.rangiffler.jupiter.annotation.CreateUser;
import com.leisan.rangiffler.jupiter.annotation.Extras;
import com.leisan.rangiffler.gql.FriendsQueryType;
import com.leisan.rangiffler.gql.api.BaseApi;
import com.leisan.rangiffler.jupiter.annotation.meta.GqlTest;
import com.leisan.rangiffler.model.testdata.TestUser;
import com.leisan.rangiffler.service.impl.AuthApiClient;
import com.leisan.rangiffler.service.impl.AuthClient;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static io.qameta.allure.SeverityLevel.CRITICAL;

@GqlTest
@Tag("graphql")
@Story("All people")
@Feature("Люди")
public class PeopleTests extends BaseApi {

    @Test
    @Severity(CRITICAL)
    @CreateExtrasUsers({@CreateUser, @CreateUser, @CreateUser})
    @DisplayName("Поиск человека в списке людей")
    void searchPeopleTest(@Extras TestUser[] users, @AuthClient AuthApiClient client1) {
        String userName = users[1].getUsername();
        String extraUserToken1 = client1.doLogin(users[0].getUsername(), users[0].getTestData().password());
        Response response = FRIENDS_API.executeFriendsQuery(FriendsQueryType.PEOPLE, extraUserToken1, 0, 10, userName);

        friendSteps.assertPeopleResponse(
                response,
                Collections.singletonList(users[1]),
                false,
                false,
                Collections.singletonList(userName),
                "ru",
                "Russian Federation",
                Collections.singletonList(null));
    }
}