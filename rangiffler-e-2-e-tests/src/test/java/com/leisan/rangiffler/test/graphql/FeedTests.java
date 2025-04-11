package com.leisan.rangiffler.test.graphql;

import com.leisan.rangiffler.gql.dto.CountryResponse;
import com.leisan.rangiffler.gql.dto.LikeResponse;
import com.leisan.rangiffler.jupiter.annotation.CreateExtrasUsers;
import com.leisan.rangiffler.jupiter.annotation.CreateUser;
import com.leisan.rangiffler.jupiter.annotation.Extras;
import com.leisan.rangiffler.model.testdata.TestUser;
import com.leisan.rangiffler.service.impl.AuthApiClient;
import com.leisan.rangiffler.gql.FriendshipActionType;
import com.leisan.rangiffler.gql.api.BaseApi;
import com.leisan.rangiffler.gql.dto.FeedResponse;
import com.leisan.rangiffler.gql.steps.MyTravelsSteps;
import com.leisan.rangiffler.jupiter.annotation.meta.GqlTest;
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
import java.util.List;

import static io.qameta.allure.SeverityLevel.BLOCKER;
import static io.qameta.allure.SeverityLevel.CRITICAL;
import static io.qameta.allure.SeverityLevel.NORMAL;
import static org.assertj.core.api.Assertions.assertThat;

@GqlTest
@Tag("graphql")
@Story("Friends")
@Feature("Лента. API")
public class FeedTests extends BaseApi {
    public FeedTests() {
    }

    private final String ruPhoto = "txt/ruPhoto.txt";
    private final String usPhoto = "txt/usPhoto.txt";
    private final String ruDesc = "Москва. Юху!";
    private final String ruCode = "ru";
    private final String ruName = "Russian Federation";
    private final String usDesc = "В Вашингтоне :)))";
    private final String usCode = "us";
    private final String usName = "United States";

    @Test
    @Severity(BLOCKER)
    @Story("Лента")
    @CreateExtrasUsers({@CreateUser, @CreateUser})
    @DisplayName("Получение ленты без друзей")
    void getFeedWithoutFriendsTest(@Extras TestUser[] users, @AuthClient AuthApiClient client1, @AuthClient AuthApiClient client2) {
        String extraUserToken1 = client1.doLogin(users[0].getUsername(), users[0].getTestData().password());
        friendSteps.executeAction(extraUserToken1, FriendshipActionType.ADD, users[1].getUsername());
        String photo1 = myTravelsSteps.createPhotoAndGetId(extraUserToken1, ruPhoto, ruDesc, ruCode);

        String extraUserToken2 = client2.doLogin(users[1].getUsername(), users[1].getTestData().password());
        friendSteps.executeAction(extraUserToken2, FriendshipActionType.ACCEPT, users[0].getUsername());
        TRAVELS_API.createPhoto(extraUserToken2, usPhoto, usDesc, usCode);

        FeedResponse feedResponse = TRAVELS_API.getFeedWithoutFriends(extraUserToken1, 0, 10).as(FeedResponse.class);
        List<MyTravelsSteps.ExpectedPhoto> expectedPhotos = List.of(
                new MyTravelsSteps.ExpectedPhoto(
                        photo1,
                        ruPhoto,
                        ruCode,
                        ruName,
                        ruDesc,
                        0,
                        List.of()));
        List<MyTravelsSteps.ExpectedStat> expectedStats = List.of(
                new MyTravelsSteps.ExpectedStat(1, ruCode));

        myTravelsSteps.assertFeedResponse(
                feedResponse,
                expectedPhotos,
                expectedStats,
                false,
                false
        );
    }

    @Test
    @Severity(CRITICAL)
    @Story("Лента")
    @CreateExtrasUsers({@CreateUser, @CreateUser})
    @DisplayName("Получение ленты с друзьями")
    void getFeedWithFriendsTest(@Extras TestUser[] users, @AuthClient AuthApiClient client1, @AuthClient AuthApiClient client2) {
        String extraUserToken1 = client1.doLogin(users[0].getUsername(), users[0].getTestData().password());
        friendSteps.executeAction(extraUserToken1, FriendshipActionType.ADD, users[1].getUsername());
        String photo1 = myTravelsSteps.createPhotoAndGetId(extraUserToken1, ruPhoto, ruDesc, ruCode);

        String extraUserToken2 = client2.doLogin(users[1].getUsername(), users[1].getTestData().password());
        friendSteps.executeAction(extraUserToken2, FriendshipActionType.ACCEPT, users[0].getUsername());
        String photo2 = myTravelsSteps.createPhotoAndGetId(extraUserToken2, usPhoto, usDesc, usCode);

        FeedResponse feedResponse = TRAVELS_API.getFeedWithFriends(extraUserToken2, 0, 10).as(FeedResponse.class);
        List<MyTravelsSteps.ExpectedPhoto> expectedPhotos = Arrays.asList(
                new MyTravelsSteps.ExpectedPhoto(
                        photo1,
                        ruPhoto,
                        ruCode,
                        ruName,
                        ruDesc,
                        0,
                        List.of()
                ),
                new MyTravelsSteps.ExpectedPhoto(
                        photo2,
                        usPhoto,
                        usCode,
                        usName,
                        usDesc,
                        0,
                        List.of()
                ));
        List<MyTravelsSteps.ExpectedStat> expectedStats = Arrays.asList(
                new MyTravelsSteps.ExpectedStat(1, ruCode),
                new MyTravelsSteps.ExpectedStat(1, usCode)
        );

        myTravelsSteps.assertFeedResponse(
                feedResponse,
                expectedPhotos,
                expectedStats,
                false,
                false
        );
    }

    @Test
    @Severity(NORMAL)
    @Story("Лента")
    @CreateExtrasUsers({@CreateUser, @CreateUser})
    @DisplayName("Пагинация ленты с друзьями")
    void feedPaginationTest(@Extras TestUser[] users, @AuthClient AuthApiClient client1, @AuthClient AuthApiClient client2) {
        String extraUserToken1 = client1.doLogin(users[0].getUsername(), users[0].getTestData().password());
        friendSteps.executeAction(extraUserToken1, FriendshipActionType.ADD, users[1].getUsername());
        String photo1 = myTravelsSteps.createPhotoAndGetId(extraUserToken1, ruPhoto, ruDesc, ruCode);

        String extraUserToken2 = client2.doLogin(users[1].getUsername(), users[1].getTestData().password());
        friendSteps.executeAction(extraUserToken2, FriendshipActionType.ACCEPT, users[0].getUsername());
        String photo2 = myTravelsSteps.createPhotoAndGetId(extraUserToken2, usPhoto, usDesc, usCode);

        FeedResponse feedResponse = TRAVELS_API.getFeedWithFriends(extraUserToken2, 0, 1).as(FeedResponse.class);
        List<MyTravelsSteps.ExpectedPhoto> expectedPhotos = Collections.singletonList(
                new MyTravelsSteps.ExpectedPhoto(
                        photo1,
                        ruPhoto,
                        ruCode,
                        ruName,
                        ruDesc,
                        0,
                        List.of()
                ));
        List<MyTravelsSteps.ExpectedStat> expectedStats = Arrays.asList(
                new MyTravelsSteps.ExpectedStat(1, ruCode),
                new MyTravelsSteps.ExpectedStat(1, usCode)
        );

        myTravelsSteps.assertFeedResponse(
                feedResponse,
                expectedPhotos,
                expectedStats,
                false,
                true
        );

        Response responseSecondPage = TRAVELS_API.getFeedWithFriends(extraUserToken2, 1, 1);
        FeedResponse feedResponseSecondPage = responseSecondPage.as(FeedResponse.class);
        List<MyTravelsSteps.ExpectedPhoto> expectedPhotosSecondPage = List.of(
                new MyTravelsSteps.ExpectedPhoto(
                        photo2,
                        usPhoto,
                        usCode,
                        usName,
                        usDesc,
                        0,
                        List.of()
                ));
        List<MyTravelsSteps.ExpectedStat> expectedStatsSecondPage = Arrays.asList(
                new MyTravelsSteps.ExpectedStat(1, ruCode),
                new MyTravelsSteps.ExpectedStat(1, usCode)
        );

        myTravelsSteps.assertFeedResponse(
                feedResponseSecondPage,
                expectedPhotosSecondPage,
                expectedStatsSecondPage,
                true,
                true
        );
    }

    @Test
    @CreateExtrasUsers(@CreateUser)
    @Severity(BLOCKER)
    @Story("Страны")
    @DisplayName("Получение списка стран")
    void getCountriesTest(@Extras TestUser[] users, @AuthClient AuthApiClient client1) {
        String token = client1.doLogin(users[0].getUsername(), users[0].getTestData().password());
        CountryResponse response = TRAVELS_API.getCountries(token).as(CountryResponse.class);
        myTravelsSteps.assertCountriesAreValid(response);
    }


    @Test
    @Severity(NORMAL)
    @Story("Статистика страны")
    @CreateExtrasUsers({@CreateUser, @CreateUser})
    @DisplayName("Статистика страны увеличивается при добавлении фото")
    void countryStatsIncreaseTest(@Extras TestUser[] users, @AuthClient AuthApiClient client1, @AuthClient AuthApiClient client2) {
        String extraUserToken1 = client1.doLogin(users[0].getUsername(), users[0].getTestData().password());
        friendSteps.executeAction(extraUserToken1, FriendshipActionType.ADD, users[1].getUsername());
        myTravelsSteps.createPhotoAndGetId(extraUserToken1, ruPhoto, ruDesc, ruCode);

        String extraUserToken2 = client2.doLogin(users[1].getUsername(), users[1].getTestData().password());
        friendSteps.executeAction(extraUserToken2, FriendshipActionType.ACCEPT, users[0].getUsername());

        FeedResponse feedResponse = TRAVELS_API.getFeedWithFriends(extraUserToken1, 0, 10).as(FeedResponse.class);
        myTravelsSteps.countriesStatAssert(feedResponse, List.of(new MyTravelsSteps.ExpectedStat(1, ruCode)));

        myTravelsSteps.createPhotoAndGetId(extraUserToken1, ruPhoto, ruDesc, ruCode);
        FeedResponse feedResponseSecond = TRAVELS_API.getFeedWithFriends(extraUserToken2, 0, 10).as(FeedResponse.class);
        myTravelsSteps.countriesStatAssert(feedResponseSecond, List.of(new MyTravelsSteps.ExpectedStat(2, ruCode)));

        myTravelsSteps.createPhotoAndGetId(extraUserToken1, ruPhoto, ruDesc, ruCode);
        FeedResponse feedResponseThird = TRAVELS_API.getFeedWithFriends(extraUserToken1, 0, 10).as(FeedResponse.class);
        myTravelsSteps.countriesStatAssert(feedResponseThird, List.of(new MyTravelsSteps.ExpectedStat(3, ruCode)));

        myTravelsSteps.createPhotoAndGetId(extraUserToken1, ruPhoto, ruDesc, ruCode);
        FeedResponse feedResponseFourth = TRAVELS_API.getFeedWithFriends(extraUserToken2, 0, 10).as(FeedResponse.class);
        myTravelsSteps.countriesStatAssert(feedResponseFourth, List.of(new MyTravelsSteps.ExpectedStat(4, ruCode)));
    }

    @Test
    @CreateExtrasUsers(@CreateUser)
    @Severity(NORMAL)
    @Story("Статистика страны")
    @DisplayName("Статистика страны уменьшается при удалении фото")
    void countryStatsDecreaseTest(@Extras TestUser[] users, @AuthClient AuthApiClient client1) {
        String extraUserToken1 = client1.doLogin(users[0].getUsername(), users[0].getTestData().password());
        String photoId = myTravelsSteps.createPhotoAndGetId(extraUserToken1, ruPhoto, ruDesc, ruCode);
        myTravelsSteps.createPhotoAndGetId(extraUserToken1, ruPhoto, ruDesc, ruCode);
        FeedResponse feedResponse = TRAVELS_API.getFeedWithFriends(extraUserToken1, 0, 10).as(FeedResponse.class);
        myTravelsSteps.countriesStatAssert(feedResponse, List.of(new MyTravelsSteps.ExpectedStat(2, ruCode)));

        TRAVELS_API.deletePhoto(extraUserToken1, photoId);
        FeedResponse feedResponseSecond = TRAVELS_API.getFeedWithFriends(extraUserToken1, 0, 10).as(FeedResponse.class);
        myTravelsSteps.countriesStatAssert(feedResponseSecond, List.of(new MyTravelsSteps.ExpectedStat(1, ruCode)));
    }

    @Test
    @Severity(CRITICAL)
    @Story("Лайки")
    @CreateExtrasUsers({@CreateUser, @CreateUser})
    @DisplayName("Установка лайка")
    void setLikeTest(@Extras TestUser[] users, @AuthClient AuthApiClient client1, @AuthClient AuthApiClient client2) {
        String extraUserToken1 = client1.doLogin(users[0].getUsername(), users[0].getTestData().password());
        friendSteps.executeAction(extraUserToken1, FriendshipActionType.ADD, users[1].getUsername());
        String photoId = myTravelsSteps.createPhotoAndGetId(extraUserToken1, ruPhoto, ruDesc, ruCode);

        String extraUserToken2 = client2.doLogin(users[1].getUsername(), users[1].getTestData().password());
        friendSteps.executeAction(extraUserToken2, FriendshipActionType.ACCEPT, users[0].getUsername());

        LikeResponse likeResponse = TRAVELS_API.likePhoto(extraUserToken2, photoId, users[0].getUsername()).as(LikeResponse.class);
        assertThat(likeResponse.getData().getPhoto().getId()).isEqualTo(photoId);
        assertThat(likeResponse.getData().getPhoto().getDescription()).isEqualTo(ruDesc);
        assertThat(likeResponse.getData().getPhoto().getLikes().getTotal()).isEqualTo(1);
        assertThat(likeResponse.getData().getPhoto().getCountry().getCode()).isEqualTo(ruCode);

        FeedResponse feedResponse = TRAVELS_API.getFeedWithFriends(extraUserToken2, 0, 10).as(FeedResponse.class);
        List<MyTravelsSteps.ExpectedPhoto> expectedPhotos = List.of(
                new MyTravelsSteps.ExpectedPhoto(
                        photoId,
                        ruPhoto,
                        ruCode,
                        ruName,
                        ruDesc,
                        1,
                        List.of(users[0].getUsername())
                ));
        List<MyTravelsSteps.ExpectedStat> expectedStats = List.of(
                new MyTravelsSteps.ExpectedStat(1, ruCode)
        );

        myTravelsSteps.assertFeedResponse(
                feedResponse,
                expectedPhotos,
                expectedStats,
                false,
                false
        );
    }

    @Test
    @Severity(CRITICAL)
    @Story("Лайки")
    @CreateExtrasUsers({@CreateUser, @CreateUser})
    @DisplayName("Удаление лайка")
    void deleteLikeTest(@Extras TestUser[] users, @AuthClient AuthApiClient client1, @AuthClient AuthApiClient client2) {
        String extraUserToken1 = client1.doLogin(users[0].getUsername(), users[0].getTestData().password());
        friendSteps.executeAction(extraUserToken1, FriendshipActionType.ADD, users[1].getUsername());
        String photoId = myTravelsSteps.createPhotoAndGetId(extraUserToken1, ruPhoto, ruDesc, ruCode);

        String extraUserToken2 = client2.doLogin(users[1].getUsername(), users[1].getTestData().password());
        friendSteps.executeAction(extraUserToken2, FriendshipActionType.ACCEPT, users[0].getUsername());

        TRAVELS_API.likePhoto(extraUserToken2, photoId, users[0].getUsername()).as(LikeResponse.class);
        LikeResponse likeResponse = TRAVELS_API.likePhoto(extraUserToken2, photoId, users[0].getUsername()).as(LikeResponse.class);
        assertThat(likeResponse.getData().getPhoto().getId()).isEqualTo(photoId);
        assertThat(likeResponse.getData().getPhoto().getDescription()).isEqualTo(ruDesc);
        assertThat(likeResponse.getData().getPhoto().getLikes().getTotal()).isEqualTo(0);
        assertThat(likeResponse.getData().getPhoto().getCountry().getCode()).isEqualTo(ruCode);

        FeedResponse feedResponse = TRAVELS_API.getFeedWithFriends(extraUserToken2, 0, 10).as(FeedResponse.class);
        List<MyTravelsSteps.ExpectedPhoto> expectedPhotos = List.of(
                new MyTravelsSteps.ExpectedPhoto(
                        photoId,
                        ruPhoto,
                        ruCode,
                        ruName,
                        ruDesc,
                        0,
                        List.of()
                ));
        List<MyTravelsSteps.ExpectedStat> expectedStats = List.of(
                new MyTravelsSteps.ExpectedStat(1, ruCode)
        );

        myTravelsSteps.assertFeedResponse(
                feedResponse,
                expectedPhotos,
                expectedStats,
                false,
                false
        );
    }
}