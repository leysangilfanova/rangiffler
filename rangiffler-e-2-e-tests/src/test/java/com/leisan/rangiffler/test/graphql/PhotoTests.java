package com.leisan.rangiffler.test.graphql;

import com.leisan.rangiffler.jupiter.annotation.CreateUser;
import com.leisan.rangiffler.jupiter.annotation.RestApiLogin;
import com.leisan.rangiffler.jupiter.annotation.Token;
import com.leisan.rangiffler.service.impl.AuthApiClient;
import com.leisan.rangiffler.gql.api.BaseApi;
import com.leisan.rangiffler.gql.dto.FeedResponse;
import com.leisan.rangiffler.gql.dto.PhotoResponse;
import com.leisan.rangiffler.gql.steps.MyTravelsSteps;
import com.leisan.rangiffler.jupiter.annotation.meta.GqlTest;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static io.qameta.allure.SeverityLevel.BLOCKER;
import static io.qameta.allure.SeverityLevel.CRITICAL;
import static io.qameta.allure.SeverityLevel.MINOR;
import static io.qameta.allure.SeverityLevel.NORMAL;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertNull;

@GqlTest
@Tag("graphql")
@Story("Friends")
@Feature("Лента. API")
public class PhotoTests extends BaseApi {
    public PhotoTests() throws IOException {
    }

    private final String ruPhoto = "txt/ruPhoto.txt";
    private final String usPhoto ="txt/usPhoto.txt";
    private final String ruDesc = "Москва. Юху!";
    private final String ruCode = "ru";
    private final String ruName = "Russian Federation";
    private final String usDesc = "В Вашингтоне :)))";
    private final String usCode = "us";
    private final String usName = "USA";

    @Test
    @RestApiLogin
    @CreateUser
    @Severity(BLOCKER)
    @Story("Посты")
    @DisplayName("Создание поста")
    void createPhotoTest(@Token String token) {
        Response response = TRAVELS_API.createPhoto(token, ruPhoto, ruDesc, ruCode);
        PhotoResponse photoResponse = response.as(PhotoResponse.class);
        myTravelsSteps.createPhotoAssert(
                photoResponse,
                ruPhoto,
                ruDesc,
                ruCode,
                0,
                response
        );
    }

    @Test
    @RestApiLogin
    @CreateUser
    @Severity(NORMAL)
    @Story("Посты")
    @DisplayName("Создание поста без описания")
    void createPhotoWithoutDescTest(@Token String token) {
        Response response = TRAVELS_API.createPhoto(token, ruPhoto, null, ruCode);
        PhotoResponse photoResponse = response.as(PhotoResponse.class);
        myTravelsSteps.createPhotoAssert(
                photoResponse,
                ruPhoto,
                null,
                ruCode,
                0,
                response
        );
    }

    @Test
    @RestApiLogin
    @CreateUser
    @Severity(MINOR)
    @Story("Посты")
    @DisplayName("Создание поста без фото")
    void createPhotoWithoutPhotoTest(@Token String token) {
        Response response = TRAVELS_API.createPhoto(token, null, ruDesc, ruCode);
        PhotoResponse photoResponse = response.as(PhotoResponse.class);
        myTravelsSteps.createPhotoAssert(
                photoResponse,
                null,
                ruDesc,
                ruCode,
                0,
                response
        );
    }

    @Test
    @RestApiLogin
    @CreateUser
    @Severity(MINOR)
    @Story("Посты")
    @DisplayName("Создание поста без кода страны")
    void createPhotoWithoutCodeTest(@Token String token) {
        Response response = TRAVELS_API.createPhoto(token, ruPhoto, ruDesc, null);
        PhotoResponse photoResponse = response.as(PhotoResponse.class);
        myTravelsSteps.createPhotoAssert(
                photoResponse,
                ruPhoto,
                ruDesc,
                null,
                0,
                response
        );
    }

    @Test
    @RestApiLogin
    @CreateUser
    @Severity(BLOCKER)
    @Story("Посты")
    @DisplayName("Удаление поста")
    void deletePhotoTest(@Token String token) {
        Response createResponse = TRAVELS_API.createPhoto(token, ruPhoto, ruDesc, ruCode);
        String photoId = createResponse.as(PhotoResponse.class).getData().getPhoto().getId();

        Response deleteResponse = TRAVELS_API.deletePhoto(token, photoId);
        assertThat("Фото удалилось", deleteResponse.path("data.deletePhoto"), equalTo(null));
        assertNull(deleteResponse.path("errors"), "Ответ содержит ошибку (errors): " + deleteResponse.body().asString());
        Response response = TRAVELS_API.getFeedWithoutFriends(token, 0, 10);
        FeedResponse feedResponse = response.as(FeedResponse.class);
        Assertions.assertThat(feedResponse.getData()).isNotNull();
    }

    @Test
    @RestApiLogin
    @CreateUser
    @Severity(BLOCKER)
    @Story("Посты")
    @DisplayName("Удаление поста")
    void deletePhotoWithIncorrectIdTest(@Token String token) {
        Response deleteResponse = TRAVELS_API.deletePhoto(token, "photoId");
        assertThat("Фото удалилось", deleteResponse.path("data.deletePhoto"), equalTo(null));
        assertNull(deleteResponse.path("errors"), "Ответ содержит ошибку (errors): " + deleteResponse.body().asString());
    }

    @Test
    @RestApiLogin
    @CreateUser
    @Severity(CRITICAL)
    @Story("Посты")
    @DisplayName("Изменение поста")
    void updatePhotoTest(@Token String token) {
        String photoId = TRAVELS_API.createPhoto(token, ruPhoto, ruDesc, ruCode)
                .as(PhotoResponse.class).getData().getPhoto().getId();

        Response editResponse = TRAVELS_API.updatePhoto(token, photoId, usPhoto, usDesc, usCode);
        PhotoResponse photoResponse = editResponse.as(PhotoResponse.class);
        myTravelsSteps.createPhotoAssert(
                photoResponse,
                usPhoto,
                usDesc,
                usCode,
                0,
                editResponse
        );

        Response response = TRAVELS_API.getFeedWithFriends(token, 0, 1);
        FeedResponse feedResponse = response.as(FeedResponse.class);

        List<MyTravelsSteps.ExpectedPhoto> expectedPhoto = List.of(
                new MyTravelsSteps.ExpectedPhoto(
                        "5ce039a3-18b6-4899-a962-b58e8abc23d3",
                        usPhoto,
                        usCode,
                        usName,
                        usDesc,
                        2,
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
                false
        );
    }

    @Test
    @RestApiLogin
    @CreateUser
    @Severity(NORMAL)
    @Story("Посты")
    @DisplayName("Изменение поста. Только фото")
    void updatePhotoOnlyPhotoTest(@Token String token) {
        String photoId = TRAVELS_API.createPhoto(token, ruPhoto, ruDesc, ruCode)
                .as(PhotoResponse.class).getData().getPhoto().getId();

        Response editResponse = TRAVELS_API.updatePhoto(token, photoId, usPhoto, ruDesc, ruCode);
        PhotoResponse photoResponse = editResponse.as(PhotoResponse.class);
        myTravelsSteps.createPhotoAssert(
                photoResponse,
                usPhoto,
                ruDesc,
                ruCode,
                0,
                editResponse
        );

        Response response = TRAVELS_API.getFeedWithFriends(token, 0, 1);
        FeedResponse feedResponse = response.as(FeedResponse.class);

        List<MyTravelsSteps.ExpectedPhoto> expectedPhoto = List.of(
                new MyTravelsSteps.ExpectedPhoto(
                        "5ce039a3-18b6-4899-a962-b58e8abc23d3",
                        usPhoto,
                        ruCode,
                        ruName,
                        ruDesc,
                        2,
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
                false
        );
    }

    @Test
    @RestApiLogin
    @CreateUser
    @Severity(MINOR)
    @Story("Посты")
    @DisplayName("Изменение поста. Только описание")
    void updatePhotoOnlyDescTest(@Token String token) {
        String photoId = TRAVELS_API.createPhoto(token, ruPhoto, ruDesc, ruCode)
                .as(PhotoResponse.class).getData().getPhoto().getId();

        Response editResponse = TRAVELS_API.updatePhoto(token, photoId, ruPhoto, usDesc, ruCode);
        PhotoResponse photoResponse = editResponse.as(PhotoResponse.class);
        myTravelsSteps.createPhotoAssert(
                photoResponse,
                ruPhoto,
                usDesc,
                ruCode,
                0,
                editResponse
        );

        Response response = TRAVELS_API.getFeedWithFriends(token, 0, 1);
        FeedResponse feedResponse = response.as(FeedResponse.class);

        List<MyTravelsSteps.ExpectedPhoto> expectedPhoto = List.of(
                new MyTravelsSteps.ExpectedPhoto(
                        "5ce039a3-18b6-4899-a962-b58e8abc23d3",
                        ruPhoto,
                        ruCode,
                        ruName,
                        usDesc,
                        2,
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
                false
        );
    }

    @Test
    @RestApiLogin
    @CreateUser
    @Severity(NORMAL)
    @Story("Посты")
    @DisplayName("Изменение поста. Только код страны")
    void updatePhotoOnlyCountryCodeTest(@Token String token) {
        String photoId = TRAVELS_API.createPhoto(token, ruPhoto, ruDesc, ruCode)
                .as(PhotoResponse.class).getData().getPhoto().getId();

        Response editResponse = TRAVELS_API.updatePhoto(token, photoId, ruPhoto, ruDesc, usCode);
        PhotoResponse photoResponse = editResponse.as(PhotoResponse.class);
        myTravelsSteps.createPhotoAssert(
                photoResponse,
                ruPhoto,
                ruDesc,
                usCode,
                0,
                editResponse
        );

        Response response = TRAVELS_API.getFeedWithFriends(token, 0, 1);
        FeedResponse feedResponse = response.as(FeedResponse.class);

        List<MyTravelsSteps.ExpectedPhoto> expectedPhoto = List.of(
                new MyTravelsSteps.ExpectedPhoto(
                        "5ce039a3-18b6-4899-a962-b58e8abc23d3",
                        ruPhoto,
                        usCode,
                        ruName,
                        ruDesc,
                        2,
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
                false
        );
    }
}