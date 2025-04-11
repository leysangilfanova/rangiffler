package com.leisan.rangiffler.page;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.leisan.rangiffler.config.Config;
import com.leisan.rangiffler.model.testdata.TestUser;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.sleep;

public class LoginPage extends BasePage<LoginPage> {
    private static final Config CFG = Config.getInstance();
    public static final String URL = CFG.authUrl() + "/login";

    private final SelenideElement loginButton = $x("//button[text()='Login']");
    private final SelenideElement usernameInput = $("input[name='username']");
    private final SelenideElement passwordInput = $("input[name='password']");
    private final SelenideElement submitButton = $("button[type='submit']");
    private final SelenideElement registerButton = $(byText("Register"));
    private final SelenideElement errorContainer = $(".form__error");

    @Nonnull
    @Step("Зарегистрироваться")
    public RegisterPage doRegister() {
        sleep(1000);
        registerButton.click();
        return new RegisterPage();
    }

    @Nonnull
    @Step("Кликнуть по кнопке 'Login'")
    public LoginPage clickLoginButton() {
        loginButton.click();
        return this;
    }

    @Nonnull
    @Step("Заполнить форму авторизации: username: {0}, password: {1}")
    public LoginPage fillLoginPage(String login, String password) {
        setUsername(login);
        setPassword(password);
        return this;
    }

    @Nonnull
    @Step("Заполнить username: {0}")
    public LoginPage setUsername(String username) {
        usernameInput.setValue(username);
        return this;
    }

    @Nonnull
    @Step("Заполнить password: {0}")
    public LoginPage setPassword(String password) {
        passwordInput.setValue(password);
        return this;
    }

    @Nonnull
    @Step("Нажать кнопку войти")
    public <T extends BasePage<?>> T submit(T expectedPage) {
        submitButton.click();
        return expectedPage;
    }

    @Nonnull
    @Step("Проверить ошибку: {error}")
    public LoginPage checkError(String error) {
        errorContainer.shouldHave(text(error));
        return this;
    }

    @Nonnull
    @Step("Авторизоваться")
    public MainPage login(TestUser user) {
        Selenide.open(LoginPage.URL, LoginPage.class)
                .clickLoginButton()
                .fillLoginPage(user.getUsername(), user.getTestData().password())
                .submit(new MainPage());
        return new MainPage();
    }

    @Nonnull
    @Step("Авторизоваться")
    public MainPage login(String username, String password) {
        Selenide.open(LoginPage.URL, LoginPage.class)
                .clickLoginButton()
                .fillLoginPage(username, password)
                .submit(new MainPage());
        return new MainPage();
    }
}
