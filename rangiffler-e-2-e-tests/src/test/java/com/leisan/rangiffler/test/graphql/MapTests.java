package com.leisan.rangiffler.test.graphql;

import com.leisan.rangiffler.gql.FriendshipActionType;
import com.leisan.rangiffler.gql.api.BaseApi;
import com.leisan.rangiffler.gql.dto.CountryResponse;
import com.leisan.rangiffler.gql.dto.FeedResponse;
import com.leisan.rangiffler.gql.steps.MyTravelsSteps;
import com.leisan.rangiffler.jupiter.annotation.CreateExtrasUsers;
import com.leisan.rangiffler.jupiter.annotation.CreateUser;
import com.leisan.rangiffler.jupiter.annotation.Extras;
import com.leisan.rangiffler.jupiter.annotation.meta.GqlTest;
import com.leisan.rangiffler.model.testdata.TestUser;
import com.leisan.rangiffler.service.impl.AuthApiClient;
import com.leisan.rangiffler.service.impl.AuthClient;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.qameta.allure.SeverityLevel.BLOCKER;
import static io.qameta.allure.SeverityLevel.NORMAL;

@GqlTest
@Tag("graphql")
@Story("Карта")
@Feature("Лента. API")
public class MapTests extends BaseApi {
    public MapTests() {
    }

    private final String ruPhoto = "txt/ruPhoto.txt";
    private final String ruDesc = "Москва. Юху!";
    private final String ruCode = "ru";

    @Test
    @CreateExtrasUsers(@CreateUser)
    @Severity(BLOCKER)
    @DisplayName("Получение списка стран")
    void getCountriesTest(@Extras TestUser[] users, @AuthClient AuthApiClient client1) {
        String token = client1.doLogin(users[0].getUsername(), users[0].getTestData().password());
        CountryResponse response = TRAVELS_API.getCountries(token).as(CountryResponse.class);
        myTravelsSteps.assertCountriesAreValid(response);
    }

    @Test
    @Severity(NORMAL)
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
}