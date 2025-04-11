package com.leisan.rangiffler.test.graphql;

import com.leisan.rangiffler.jupiter.annotation.CreateUser;
import com.leisan.rangiffler.jupiter.annotation.RestApiLogin;
import com.leisan.rangiffler.jupiter.annotation.Token;
import com.leisan.rangiffler.model.testdata.TestUser;
import io.restassured.response.Response;

import static io.qameta.allure.SeverityLevel.NORMAL;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import com.leisan.rangiffler.gql.api.BaseApi;
import com.leisan.rangiffler.jupiter.annotation.meta.GqlTest;
import com.leisan.rangiffler.utils.FileUtils;
import com.leisan.rangiffler.utils.RandomDataUtils;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static io.qameta.allure.SeverityLevel.CRITICAL;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertNull;

@GqlTest
@Tag("graphql")
@Story("All people")
@Feature("Профиль. API")
public class ProfileTests extends BaseApi {

    private static Stream<Arguments> updateUserData() {
        return Stream.of(
                Arguments.of("Изменение имени", RandomDataUtils.randomName(), null, null, null),
                Arguments.of("Изменение фамилии", null, RandomDataUtils.randomSurname(), null, null),
                Arguments.of("Изменение аватара", null, null, FileUtils.readResourceFile("avatar_base64.txt"), null),
                Arguments.of("Изменение локации", null, null, null, "us")
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("updateUserData")
    @RestApiLogin
    @CreateUser
    @Severity(NORMAL)
    void updateUserProfileTest(String displayName,
                               String firstName,
                               String surName,
                               String avatar,
                               String location,
                               @Token String token,
                               TestUser user) {
        Response userUpdateResponse = USER_API.updateUser(token, firstName, surName, avatar, location);
        updateUserCheck(userUpdateResponse, user.getUsername(), firstName, surName, avatar, location);

        Response userInfoResponse = USER_API.updateUser(token, firstName, surName, avatar, location);
        updateUserCheck(userInfoResponse, user.getUsername(), firstName, surName, avatar, location);
    }

    @Test
    @RestApiLogin
    @CreateUser
    @Severity(CRITICAL)
    @DisplayName("Изменение данных в профиле")
    void updateProfileTest(@Token String token, TestUser user) {
        String avatar = FileUtils.readResourceFile("avatar_base64.txt");
        String firstName = RandomDataUtils.randomName();
        String surName = RandomDataUtils.randomSurname();
        String location = "us";

        Response userUpdateResponse = USER_API.updateUser(token, firstName, surName, avatar, location);
        updateUserCheck(userUpdateResponse, user.getUsername(), firstName, surName, avatar, location);

        Response userInfoResponse = USER_API.updateUser(token, firstName, surName, avatar, location);
        updateUserCheck(userInfoResponse, user.getUsername(), firstName, surName, avatar, location);
    }

    public static void updateUserCheck(Response response,
                                       String expectedUsername,
                                       String expectedFirstname,
                                       String expectedSurname,
                                       String expectedAvatar,
                                       String expectedLocationCode) {
        String id = response.path("data.user.id");
        String username = response.path("data.user.username");
        String firstname = response.path("data.user.firstname");
        String surname = response.path("data.user.surname");
        String avatar = response.path("data.user.avatar");
        String locationCode = response.path("data.user.location.code");
        String locationType = response.path("data.user.location.__typename");
        String userType = response.path("data.user.__typename");

        assertThat("User ID should not be null or empty", id, not(isEmptyOrNullString()));
        assertThat("Username should match", username, equalTo(expectedUsername));

        if (expectedFirstname != null) {
            assertThat("Firstname should match", firstname, equalTo(expectedFirstname));
        }

        if (expectedSurname != null) {
            assertThat("Surname should match", surname, equalTo(expectedSurname));
        }

        if (expectedAvatar != null) {
            assertThat("Avatar should match", avatar, equalTo(expectedAvatar));
        }

        if (expectedLocationCode != null) {
            assertThat("Location code should match", locationCode, equalTo(expectedLocationCode));
            assertThat("Location typename should be Country", locationType, equalTo("Country"));
        }

        assertThat("User typename should be User", userType, equalTo("User"));
        assertNull(response.path("errors"), "Ответ содержит ошибку (errors): " + response.body().asString());
    }
}