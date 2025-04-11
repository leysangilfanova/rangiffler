package com.leisan.rangiffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class RegisterPage extends BasePage<RegisterPage> {

    private final SelenideElement usernameInput = $("input[name='username']");
    private final SelenideElement passwordInput = $("input[name='password']");
    private final SelenideElement passwordSubmitInput = $("input[name='passwordSubmit']");
    private final SelenideElement submitButton = $("button[type='submit']");
    private final SelenideElement proceedLoginButton = $(".form_sign-in");
    private final SelenideElement errorContainer = $(".form__error");
    private final SelenideElement registerSuccessText = $(".form__paragraph_success");

    @Nonnull
    @Step("Заполнить страницу регистрации: username: {0}, password: {1}, submit password: {2}")
    public RegisterPage fillRegisterPage(String login, String password, String passwordSubmit) {
        setUsername(login);
        setPassword(password);
        setPasswordSubmit(passwordSubmit);
        return this;
    }

    @Nonnull
    @Step("Установить username: {0}")
    public RegisterPage setUsername(String username) {
        usernameInput.setValue(username);
        return this;
    }


    @Nonnull
    @Step("Установить пароль: {0}")
    public RegisterPage setPassword(String password) {
        passwordInput.setValue(password);
        return this;
    }


    @Nonnull
    @Step("Установить подтверждение пароля: {0}")
    public RegisterPage setPasswordSubmit(String password) {
        passwordSubmitInput.setValue(password);
        return this;
    }

    @Nonnull
    @Step("Нажать кнопку войти")
    public <T extends BasePage<?>> T submit(T expectedPage) {
        submitButton.click();
        return expectedPage;
    }

    @Nonnull
    @Step("Проверка успешности регистрации")
    public RegisterPage registrationCheck() {
        registerSuccessText.shouldHave(text("Congratulations! You've registered!"));
        proceedLoginButton.shouldBe(visible);
        return this;
    }

    @Nonnull
    @Step("Проверка неуспешности регистрации")
    public RegisterPage registrationFailedCheck(String text) {
        errorContainer.shouldHave(text(text));
        return this;
    }
}
