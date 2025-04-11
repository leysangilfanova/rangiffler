package com.leisan.rangiffler.test.web;

import com.codeborne.selenide.Selenide;
import com.leisan.rangiffler.jupiter.extension.UserExtension;
import com.leisan.rangiffler.jupiter.annotation.meta.WebTest;
import com.leisan.rangiffler.page.LoginPage;
import com.leisan.rangiffler.page.RegisterPage;
import com.leisan.rangiffler.utils.RandomDataUtils;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.Story;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static io.qameta.allure.SeverityLevel.BLOCKER;
import static io.qameta.allure.SeverityLevel.CRITICAL;

@Tag("web")
@WebTest
@Feature("Регистрация")
public class RegistrationTests {

    record TestCase(String username, String password, String confirmPassword, String expectedError, String displayName) {}
    static Stream<TestCase> invalidRegistrationData() {
        return Stream.of(
                new TestCase(RandomDataUtils.randomUsername(), "1", "1",
                        "Allowed password length should be from 3 to 12 characters", "Пароль короче 3-х символов"),
                new TestCase(RandomDataUtils.randomUsername(), RandomStringUtils.randomAlphanumeric(13), RandomStringUtils.randomAlphanumeric(13),
                        "Allowed password length should be from 3 to 12 characters", "Пароль длиннее 12-и символов"),
                new TestCase("1", "123", "123",
                        "Allowed username length should be from 3 to 50 characters", "Логин короче 3-х символов"),
                new TestCase(RandomStringUtils.randomAlphanumeric(51), "123", "123",
                        "Allowed username length should be from 3 to 50 characters", "Логин длиннее 50-и символов"),
                new TestCase(RandomDataUtils.randomUsername(), "123", "321",
                        "Passwords should be equal", "Пароли разные")
        );
    }

    @ParameterizedTest(name = "{index}. {0}")
    @MethodSource("invalidRegistrationData")
    @Severity(CRITICAL)
    @Story("Неуспешная регистрация")
    void unsuccessfulRegisterTest(TestCase testCase) {
        Selenide.open(LoginPage.URL, LoginPage.class)
                .doRegister()
                .fillRegisterPage(testCase.username, testCase.password, testCase.confirmPassword)
                .submit(new RegisterPage())
                .registrationFailedCheck(testCase.expectedError);
    }

    @Test
    @Severity(BLOCKER)
    @DisplayName("Успешная регистрация")
    void successfulRegisterTest() {
        Selenide.open(LoginPage.URL, LoginPage.class)
                .doRegister()
                .fillRegisterPage(RandomDataUtils.randomUsername(), UserExtension.defaultPassword, UserExtension.defaultPassword)
                .submit(new RegisterPage())
                .registrationCheck();
    }
}