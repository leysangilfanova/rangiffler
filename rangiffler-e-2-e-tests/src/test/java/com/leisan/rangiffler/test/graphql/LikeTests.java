package com.leisan.rangiffler.test.graphql;

import com.leisan.rangiffler.gql.FriendshipActionType;
import com.leisan.rangiffler.gql.api.BaseApi;
import com.leisan.rangiffler.gql.dto.FeedResponse;
import com.leisan.rangiffler.gql.dto.LikeResponse;
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

import static io.qameta.allure.SeverityLevel.CRITICAL;
import static org.assertj.core.api.Assertions.assertThat;

@GqlTest
@Tag("graphql")
@Story("Лайки")
@Feature("Лента. API")
public class LikeTests extends BaseApi {
    public LikeTests() {
    }

    private final String ruPhoto = "txt/ruPhoto.txt";
    private final String ruDesc = "Москва. Юху!";
    private final String ruCode = "ru";
    private final String ruName = "Russian Federation";

    @Test
    @Severity(CRITICAL)
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