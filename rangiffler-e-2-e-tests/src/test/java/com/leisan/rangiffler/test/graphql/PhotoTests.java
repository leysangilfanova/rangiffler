package com.leisan.rangiffler.test.graphql;

import com.leisan.rangiffler.jupiter.annotation.CreateExtrasUsers;
import com.leisan.rangiffler.jupiter.annotation.CreateUser;
import com.leisan.rangiffler.jupiter.annotation.Extras;
import com.leisan.rangiffler.model.testdata.TestUser;
import com.leisan.rangiffler.service.impl.AuthApiClient;
import com.leisan.rangiffler.gql.api.BaseApi;
import com.leisan.rangiffler.gql.dto.FeedResponse;
import com.leisan.rangiffler.gql.dto.PhotoResponse;
import com.leisan.rangiffler.gql.steps.MyTravelsSteps;
import com.leisan.rangiffler.jupiter.annotation.meta.GqlTest;
import com.leisan.rangiffler.service.impl.AuthClient;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.qameta.allure.SeverityLevel.BLOCKER;
import static io.qameta.allure.SeverityLevel.CRITICAL;
import static io.qameta.allure.SeverityLevel.MINOR;
import static io.qameta.allure.SeverityLevel.NORMAL;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@GqlTest
@Tag("graphql")
@Story("Friends")
@Feature("Лента. API")
public class PhotoTests extends BaseApi {
    public PhotoTests() {
    }

    private final String ruPhoto = "txt/ruPhoto.txt";
    private final String usPhoto ="txt/usPhoto.txt";
    private final String ruDesc = "Москва. Юху!";
    private final String ruCode = "ru";
    private final String ruName = "Russian Federation";
    private final String usDesc = "В Вашингтоне :)))";
    private final String usCode = "us";
    private final String usName = "United States";

    @Test
    @CreateExtrasUsers(@CreateUser)
    @Severity(BLOCKER)
    @Story("Посты")
    @DisplayName("Создание поста")
    void createPhotoTest(@Extras TestUser[] users, @AuthClient AuthApiClient client) {
        String token = client.doLogin(users[0].getUsername(), users[0].getTestData().password());
        Response response = TRAVELS_API.createPhoto(token, ruPhoto, ruDesc, ruCode);
        PhotoResponse photoResponse = response.as(PhotoResponse.class);
        myTravelsSteps.createPhotoAssert(
                photoResponse,
                ruDesc,
                ruCode,
                0,
                response
        );
    }

    @Test
    @Severity(NORMAL)
    @Story("Посты")
    @CreateExtrasUsers(@CreateUser)
    @DisplayName("Создание поста без описания")
    void createPhotoWithoutDescTest(@Extras TestUser[] users, @AuthClient AuthApiClient client) {
        String token = client.doLogin(users[0].getUsername(), users[0].getTestData().password());
        Response response = TRAVELS_API.createPhoto(token, ruPhoto, "", ruCode);
        PhotoResponse photoResponse = response.as(PhotoResponse.class);
        myTravelsSteps.createPhotoAssert(
                photoResponse,
                "",
                ruCode,
                0,
                response
        );
    }

    @Test
    @Severity(MINOR)
    @Story("Посты")
    @CreateExtrasUsers(@CreateUser)
    @DisplayName("Создание поста без кода страны")
    void createPhotoWithoutCodeTest(@Extras TestUser[] users, @AuthClient AuthApiClient client) {
        String token = client.doLogin(users[0].getUsername(), users[0].getTestData().password());
        Response response = TRAVELS_API.createPhoto(token, ruPhoto, ruDesc, "");
        PhotoResponse photoResponse = response.as(PhotoResponse.class);
        assertEquals(photoResponse.getErrors().getFirst().getMessage(),
                "Exception while fetching data (/photo) : io.micronaut.http.client.exceptions.HttpClientResponseException: Unauthorized");
    }

    @Test
    @Severity(BLOCKER)
    @Story("Посты")
    @CreateExtrasUsers(@CreateUser)
    @DisplayName("Удаление поста")
    void deletePhotoTest(@Extras TestUser[] users, @AuthClient AuthApiClient client) {
        String token = client.doLogin(users[0].getUsername(), users[0].getTestData().password());
        String photoId =  myTravelsSteps.createPhotoAndGetId(token, ruPhoto, ruDesc, ruCode);

        Response deleteResponse = TRAVELS_API.deletePhoto(token, photoId);
        assertThat("Фото удалилось", deleteResponse.path("data.deletePhoto"), equalTo(true));
        assertNull(deleteResponse.path("errors"), "Ответ содержит ошибку (errors): " + deleteResponse.body().asString());
        Response response = TRAVELS_API.getFeedWithoutFriends(token, 0, 10);
        FeedResponse feedResponse = response.as(FeedResponse.class);
        Assertions.assertThat(feedResponse.getData()).isNotNull();
    }

    @Test
    @Severity(CRITICAL)
    @Story("Посты")
    @CreateExtrasUsers(@CreateUser)
    @DisplayName("Изменение поста")
    void updatePhotoTest(@Extras TestUser[] users, @AuthClient AuthApiClient client) {
        String token = client.doLogin(users[0].getUsername(), users[0].getTestData().password());
        String photoId =  myTravelsSteps.createPhotoAndGetId(token, ruPhoto, ruDesc, ruCode);

        Response editResponse = TRAVELS_API.updatePhoto(token, photoId, usPhoto, usDesc, usCode);
        PhotoResponse photoResponse = editResponse.as(PhotoResponse.class);
        myTravelsSteps.createPhotoAssert(
                photoResponse,
                usDesc,
                usCode,
                0,
                editResponse
        );

        FeedResponse feedResponse = TRAVELS_API.getFeedWithoutFriends(token, 0, 1).as(FeedResponse.class);
        List<MyTravelsSteps.ExpectedPhoto> expectedPhoto = List.of(
                new MyTravelsSteps.ExpectedPhoto(
                        photoId,
                        usPhoto,
                        usCode,
                        usName,
                        usDesc,
                        0,
                        List.of()
                ));
        List<MyTravelsSteps.ExpectedStat> expectedStat = List.of(
                new MyTravelsSteps.ExpectedStat(1, usCode)
        );

        myTravelsSteps.assertFeedResponse(
                feedResponse,
                expectedPhoto,
                expectedStat,
                false,
                true
        );
    }

    @Test
    @Severity(NORMAL)
    @Story("Посты")
    @CreateExtrasUsers(@CreateUser)
    @DisplayName("Изменение поста. Только фото")
    void updatePhotoOnlyPhotoTest(@Extras TestUser[] users, @AuthClient AuthApiClient client) {
        String token = client.doLogin(users[0].getUsername(), users[0].getTestData().password());
        String photoId = TRAVELS_API.createPhoto(token, ruPhoto, ruDesc, ruCode)
                .as(PhotoResponse.class).getData().getPhoto().getId();

        Response editResponse = TRAVELS_API.updatePhoto(token, photoId, usPhoto, ruDesc, ruCode);
        PhotoResponse photoResponse = editResponse.as(PhotoResponse.class);
        myTravelsSteps.createPhotoAssert(
                photoResponse,
                ruDesc,
                ruCode,
                0,
                editResponse
        );

        FeedResponse feedResponse = TRAVELS_API.getFeedWithoutFriends(token, 0, 1).as(FeedResponse.class);
        List<MyTravelsSteps.ExpectedPhoto> expectedPhoto = List.of(
                new MyTravelsSteps.ExpectedPhoto(
                        photoId,
                        usPhoto,
                        ruCode,
                        ruName,
                        ruDesc,
                        0,
                        List.of()
                ));
        List<MyTravelsSteps.ExpectedStat> expectedStat = List.of(
                new MyTravelsSteps.ExpectedStat(1, ruCode)
        );

        myTravelsSteps.assertFeedResponse(
                feedResponse,
                expectedPhoto,
                expectedStat,
                false,
                true
        );
    }

    @Test
    @Severity(MINOR)
    @Story("Посты")
    @CreateExtrasUsers(@CreateUser)
    @DisplayName("Изменение поста. Только описание")
    void updatePhotoOnlyDescTest(@Extras TestUser[] users, @AuthClient AuthApiClient client) {
        String token = client.doLogin(users[0].getUsername(), users[0].getTestData().password());
        String photoId = TRAVELS_API.createPhoto(token, ruPhoto, ruDesc, ruCode)
                .as(PhotoResponse.class).getData().getPhoto().getId();

        Response editResponse = TRAVELS_API.updatePhoto(token, photoId, ruPhoto, usDesc, ruCode);
        PhotoResponse photoResponse = editResponse.as(PhotoResponse.class);
        myTravelsSteps.createPhotoAssert(
                photoResponse,
                usDesc,
                ruCode,
                0,
                editResponse
        );

        FeedResponse feedResponse = TRAVELS_API.getFeedWithoutFriends(token, 0, 1).as(FeedResponse.class);
        List<MyTravelsSteps.ExpectedPhoto> expectedPhoto = List.of(
                new MyTravelsSteps.ExpectedPhoto(
                        photoId,
                        ruPhoto,
                        ruCode,
                        ruName,
                        usDesc,
                        0,
                        List.of()
                ));
        List<MyTravelsSteps.ExpectedStat> expectedStat = List.of(
                new MyTravelsSteps.ExpectedStat(1, ruCode)
        );

        myTravelsSteps.assertFeedResponse(
                feedResponse,
                expectedPhoto,
                expectedStat,
                false,
                true
        );
    }

    @Test
    @Severity(NORMAL)
    @Story("Посты")
    @CreateExtrasUsers(@CreateUser)
    @DisplayName("Изменение поста. Только код страны")
    void updatePhotoOnlyCountryCodeTest(@Extras TestUser[] users, @AuthClient AuthApiClient client) {
        String token = client.doLogin(users[0].getUsername(), users[0].getTestData().password());
        String photoId = TRAVELS_API.createPhoto(token, ruPhoto, ruDesc, ruCode)
                .as(PhotoResponse.class).getData().getPhoto().getId();

        Response editResponse = TRAVELS_API.updatePhoto(token, photoId, ruPhoto, ruDesc, usCode);
        PhotoResponse photoResponse = editResponse.as(PhotoResponse.class);
        myTravelsSteps.createPhotoAssert(
                photoResponse,
                ruDesc,
                usCode,
                0,
                editResponse
        );

        FeedResponse feedResponse = TRAVELS_API.getFeedWithoutFriends(token, 0, 1).as(FeedResponse.class);
        List<MyTravelsSteps.ExpectedPhoto> expectedPhoto = List.of(
                new MyTravelsSteps.ExpectedPhoto(
                        photoId,
                        ruPhoto,
                        usCode,
                        usName,
                        ruDesc,
                        0,
                        List.of()
                ));
        List<MyTravelsSteps.ExpectedStat> expectedStat = List.of(
                new MyTravelsSteps.ExpectedStat(1, usCode)
        );

        myTravelsSteps.assertFeedResponse(
                feedResponse,
                expectedPhoto,
                expectedStat,
                false,
                true
        );
    }
}