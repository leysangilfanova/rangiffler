package com.leisan.rangiffler.test.web;

import com.leisan.rangiffler.gql.FriendshipActionType;
import com.leisan.rangiffler.jupiter.annotation.ApiLogin;
import com.leisan.rangiffler.jupiter.annotation.CreateExtrasUsers;
import com.leisan.rangiffler.jupiter.annotation.CreateUser;
import com.leisan.rangiffler.jupiter.annotation.Extras;
import com.leisan.rangiffler.jupiter.annotation.Token;
import com.leisan.rangiffler.model.testdata.TestUser;
import com.leisan.rangiffler.jupiter.annotation.meta.WebTest;
import com.leisan.rangiffler.service.impl.AuthApiClient;
import com.leisan.rangiffler.service.impl.AuthClient;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.executeJavaScript;
import static com.leisan.rangiffler.gql.api.BaseApi.TRAVELS_API;
import static com.leisan.rangiffler.gql.api.BaseApi.friendSteps;
import static com.leisan.rangiffler.page.BasePage.myTravelsPage;
import static com.leisan.rangiffler.utils.ScreenshotComparingUtil.takeElementScreenshot;
import static io.qameta.allure.Allure.step;
import static io.qameta.allure.SeverityLevel.BLOCKER;
import static io.qameta.allure.SeverityLevel.CRITICAL;
import static io.qameta.allure.SeverityLevel.NORMAL;

@Tag("web")
@WebTest
@Feature("Карта")
public class MapTests {

    @Test
    @ApiLogin
    @CreateUser
    @Severity(BLOCKER)
    @DisplayName("При добавлении фото закрашивается страна")
    void mapFilledByPhotoTest() {
        step("До загрузки фотографий карта пустая", () ->
                takeElementScreenshot("map/emptyMap", myTravelsPage.worldMap));

        step("При загрузке фотографии заполняется страна для которой загружена фотография", () -> {
            myTravelsPage.addPhoto("img/greenland.jpg", "gl", "test");
            takeElementScreenshot("map/oneCountryFilledMap", myTravelsPage.worldMap);
        });

        step("При загрузке еще одной фотографии заполняется вторая страна", () -> {
            myTravelsPage.addPhoto("img/kazan.jpg", "ru", "test");
            takeElementScreenshot("map/twoCountryFilledMap", myTravelsPage.worldMap);
        });
    }

    @Test
    @ApiLogin
    @CreateUser
    @Severity(CRITICAL)
    @DisplayName("При удалении фото удаляется закрашивание страны")
    void mapUnfilledByPhotoTest() {
        myTravelsPage.addPhoto("img/greenland.jpg", "gl", "test")
                .clickDeleteCardButton()
                .shouldDeletePostAlert("Post deleted");
        takeElementScreenshot("map/emptyMap", myTravelsPage.worldMap);
    }

    @Test
    @ApiLogin
    @CreateUser
    @Severity(NORMAL)
    @DisplayName("При редактировании страны фото изменяется закрашенная страна")
    void countryChangedPhotoTest() {
        myTravelsPage.addPhoto("img/greenland.jpg", "gl", "test")
                .editPhoto("img/kazan.jpg", "ru", "test");
        executeJavaScript("window.scrollTo(0, 0);");
        takeElementScreenshot("map/anotherCountryFilledMap", myTravelsPage.worldMap);
    }

    @Test
    @ApiLogin
    @CreateExtrasUsers(@CreateUser)
    @CreateUser
    @Severity(BLOCKER)
    @DisplayName("Страны друзей отображаются на карте")
    void friendOnMapTest(@Extras TestUser[] users, @AuthClient AuthApiClient client, @Token String token, TestUser user) {
        String extraUserToken = client.doLogin(users[0].getUsername(), users[0].getTestData().password());
        friendSteps.executeAction(extraUserToken, FriendshipActionType.ADD, user.getUsername());
        TRAVELS_API.createPhoto(extraUserToken, "txt/usPhoto.txt", "test", "gl");
        friendSteps.executeAction(token, FriendshipActionType.ACCEPT, users[0].getUsername());

        step("Проверяем как отображается фото друга на карта", () -> {
            myTravelsPage.clickWithFriendsButton();
            takeElementScreenshot("map/oneCountryFilledMap", myTravelsPage.worldMap);
        });

        step("Во вкладке 'With Friends' так же отображаются фото пользователя", () -> {
            myTravelsPage.addPhoto("txt/ruPhoto.txt", "ru", "test");
            takeElementScreenshot("map/twoCountryFilledMap", myTravelsPage.worldMap);
        });
    }
}