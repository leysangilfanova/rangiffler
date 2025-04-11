package com.leisan.rangiffler.test.web;

import com.codeborne.selenide.Selenide;
import com.leisan.rangiffler.jupiter.annotation.CreateUser;
import com.leisan.rangiffler.model.testdata.TestUser;
import com.leisan.rangiffler.jupiter.annotation.meta.WebTest;
import com.leisan.rangiffler.page.LoginPage;
import com.leisan.rangiffler.page.MainPage;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.leisan.rangiffler.utils.RandomDataUtils.randomUsername;
import static io.qameta.allure.SeverityLevel.BLOCKER;
import static io.qameta.allure.SeverityLevel.CRITICAL;

@Tag("web")
@WebTest
@Feature("Авторизация")
public class AuthTests {

    @Test
    @Severity(BLOCKER)
    @DisplayName("Неуспешная авторизация")
    void unsuccessfulAuthTest() {
        Selenide.open(LoginPage.URL, LoginPage.class)
                .clickLoginButton()
                .fillLoginPage(randomUsername(), "BAD")
                .submit(new LoginPage())
                .checkError("Bad credentials");
    }

    @Test
    @CreateUser
    @Severity(CRITICAL)
    @DisplayName("Успешная авторизация")
    void successfulAuthTest(TestUser user) {
        Selenide.open(LoginPage.URL, LoginPage.class)
                .clickLoginButton()
                .fillLoginPage(user.getUsername(), user.getTestData().password())
                .submit(new MainPage())
                .checkThatHeaderPageLoaded();
    }
}