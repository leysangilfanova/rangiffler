package com.leisan.rangiffler.test.web;

import com.leisan.rangiffler.jupiter.annotation.ApiLogin;
import com.leisan.rangiffler.jupiter.annotation.CreateUser;
import com.leisan.rangiffler.model.testdata.TestUser;
import com.leisan.rangiffler.jupiter.annotation.meta.WebTest;
import com.leisan.rangiffler.utils.RandomDataUtils;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.leisan.rangiffler.page.BasePage.mainPage;
import static com.leisan.rangiffler.page.BasePage.myProfilePage;
import static com.leisan.rangiffler.utils.ScreenshotComparingUtil.takeElementScreenshot;
import static io.qameta.allure.SeverityLevel.MINOR;
import static io.qameta.allure.SeverityLevel.NORMAL;

@Tag("web")
@WebTest
@Feature("Профиль")
public class ProfileTests {

    @Test
    @ApiLogin
    @CreateUser
    @Severity(NORMAL)
    @DisplayName("Проверка отображения незаполненного профиля")
    void newProfileTest(TestUser user) {
        mainPage.openProfilePage()
                .usernameShouldBe(user.getUsername());
    }

    @Test
    @ApiLogin
    @CreateUser
    @Severity(NORMAL)
    @DisplayName("Изменение данных в профиле")
    void profileUpdateTest(TestUser user) {
        String firstName = RandomDataUtils.randomName();
        String surName = RandomDataUtils.randomSurname();
        mainPage.openProfilePage()
                .setAvatar("img/avatar.png")
                .setLocation("kw")
                .setFirstname(firstName)
                .setSurname(surName)
                .saveChanges()
                .checkSuccessTitle("Your profile is successfully updated");

        takeElementScreenshot("avatar", myProfilePage.avatar);
        myProfilePage.locationNameShouldBe("Kuwait")
                .firstnameShouldBe(firstName)
                .surnameShouldBe(surName)
                .usernameShouldBe(user.getUsername());
    }

    @Test
    @ApiLogin
    @CreateUser
    @Severity(MINOR)
    @DisplayName("Неудачное изменение данных в профиле")
    void profileFailedUpdateTest() {
        String firstName = RandomStringUtils.randomAlphanumeric(51);
        String surName = RandomStringUtils.randomAlphanumeric(101);
        mainPage.openProfilePage()
                .setFirstname(firstName)
                .setSurname(surName)
                .saveChanges();

        myProfilePage.checkErrorFirstnameTitle("First name length has to be not longer that 50 symbols")
                .checkErrorSurnameTitle("Surname length has to be not longer that 100 symbols");
    }
}