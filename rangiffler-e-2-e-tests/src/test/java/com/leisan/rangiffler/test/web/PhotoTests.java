package com.leisan.rangiffler.test.web;

import com.codeborne.selenide.Selenide;
import com.leisan.rangiffler.jupiter.annotation.ApiLogin;
import com.leisan.rangiffler.jupiter.annotation.CreateUser;
import com.leisan.rangiffler.jupiter.annotation.meta.WebTest;
import com.leisan.rangiffler.page.MyTravelsPage;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.open;
import static com.leisan.rangiffler.page.BasePage.myTravelsPage;
import static com.leisan.rangiffler.utils.ScreenshotComparingUtil.takeElementScreenshot;
import static io.qameta.allure.SeverityLevel.BLOCKER;
import static io.qameta.allure.SeverityLevel.CRITICAL;
import static io.qameta.allure.SeverityLevel.NORMAL;

@Tag("web")
@WebTest
@Feature("Фото")
public class PhotoTests {

    @Test
    @ApiLogin
    @CreateUser
    @Severity(BLOCKER)
    @Story("Добавление фото")
    @DisplayName("Успешное добавление фото")
    void addPhotoTest() {
        myTravelsPage.addPhoto("img/greenland.jpg", "gl", "test").postCreatedCheck();
    }

    @Test
    @ApiLogin
    @CreateUser
    @Severity(CRITICAL)
    @Story("Добавление фото")
    @DisplayName("Успешное добавление фото. Без комментария")
    void addPhotoWithoutDescriptionTest() {
        myTravelsPage.addPhoto("img/greenland.jpg", "gl", null).postCreatedCheck();
    }

    @Test
    @ApiLogin
    @CreateUser
    @Severity(NORMAL)
    @Story("Добавление фото")
    @DisplayName("Попытка добавить фото без его загрузки")
    void addPhotoWithoutPhotoUploadTest() {
        myTravelsPage.clickAddPhotoButton()
                .clickSavePhotoButton()
                .checkPhotoError("Please upload an image");
    }

    @Test
    @ApiLogin
    @CreateUser
    @Severity(BLOCKER)
    @DisplayName("Удаление фото")
    void deletePhotoTest() {
        myTravelsPage.addPhoto("img/greenland.jpg", "gl", "test")
                .clickDeleteCardButton()
                .shouldDeletePostAlert("Post deleted");
    }

    @Test
    @ApiLogin
    @Tag("screenshot")
    @CreateUser
    @Severity(NORMAL)
    @DisplayName("Изменение фото")
    void editPhotoTest() {
        Selenide.open(MyTravelsPage.URL, MyTravelsPage.class);

        myTravelsPage.addPhoto("img/greenland.jpg", "gl", "test")
                .editPhoto("img/kazan.jpg");
        takeElementScreenshot("photo", myTravelsPage.image);
    }

    @Test
    @ApiLogin
    @CreateUser
    @Severity(BLOCKER)
    @Story("Лайки на фото")
    @DisplayName("Успешная установка лайка на фото")
    void likePhotoTest() {
        myTravelsPage.addPhoto("img/greenland.jpg", "gl", "test")
                .clickLikeButton()
                .checkLikeAlert("Post was succesfully liked")
                .checkLikeCount("1");
    }

    @Test
    @ApiLogin
    @CreateUser
    @Severity(CRITICAL)
    @Story("Лайки на фото")
    @DisplayName("Удаление лайка с фото")
    void deleteLikePhotoTest() {
        myTravelsPage.addPhoto("img/greenland.jpg", "gl", "test")
                .clickLikeButton()
                .checkLikeAlert("Post was succesfully liked")
                .clickLikeButton()
                .checkLikeCount("0");
    }
}