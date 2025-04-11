package com.leisan.rangiffler.jupiter.extension;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import com.leisan.rangiffler.jupiter.annotation.ApiLogin;
import com.leisan.rangiffler.jupiter.annotation.RestApiLogin;
import com.leisan.rangiffler.jupiter.annotation.Token;
import com.leisan.rangiffler.config.Config;
import com.leisan.rangiffler.model.testdata.TestUser;
import com.leisan.rangiffler.page.MainPage;
import com.leisan.rangiffler.service.impl.AuthApiClient;
import com.leisan.rangiffler.api.core.ThreadSafeCookieStore;
import io.qameta.allure.Step;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;
import org.openqa.selenium.Cookie;

public class ApiLoginExtension implements BeforeEachCallback, AfterEachCallback, ParameterResolver {
    private static final ThreadLocal<String> CODE_HOLDER = new ThreadLocal<>();
    private static final Config CFG = Config.getInstance();
    private static final ThreadLocal<String> TOKEN_HOLDER = new ThreadLocal<>();
    private static final ThreadLocal<Boolean> TOKEN_FLAG = ThreadLocal.withInitial(() -> false);

    private final boolean setupBrowser;

    public ApiLoginExtension(boolean setupBrowser) {
        this.setupBrowser = setupBrowser;
    }

    public ApiLoginExtension() {
        this(true);
    }

    public static ApiLoginExtension rest() {
        return new ApiLoginExtension(false);
    }

    @Override
    @Step("Выполнение предварительной авторизации")
    public void beforeEach(ExtensionContext context) {
        if (requiresApiLogin(context) && !TOKEN_FLAG.get()) {
            performApiLogin();
        }
    }

    private boolean requiresApiLogin(ExtensionContext context) {
        return AnnotationSupport.isAnnotated(context.getRequiredTestMethod(), ApiLogin.class)
                && !context.getRequiredTestClass().isAnnotationPresent(RestApiLogin.class);
    }

    @Step("Выполнить API-логин")
    private void performApiLogin() {
        final TestUser user = UserExtension.getUserJson();
        String token = new AuthApiClient().doLogin(user.getUsername(), user.getTestData().password());

        TOKEN_HOLDER.set(token);
        TOKEN_FLAG.set(true);

        if (setupBrowser) {
            initializeBrowserSession(token);
        }
    }

    @Step("Инициализация браузерной сессии")
    private void initializeBrowserSession(String token) {
        Selenide.open(CFG.authUrl());
        Selenide.localStorage().setItem("id_token", token);
        WebDriverRunner.getWebDriver().manage().addCookie(jsessionIdCookie());
        Selenide.open(CFG.redirectUrl(), MainPage.class).checkThatHeaderPageLoaded();
    }

    private Cookie jsessionIdCookie() {
        return new Cookie("JSESSIONID", ThreadSafeCookieStore.INSTANCE.cookieValue("JSESSIONID"));
    }

    @Override
    public void afterEach(ExtensionContext context) {
        TOKEN_HOLDER.remove();
        TOKEN_FLAG.remove();
        CODE_HOLDER.remove();
    }

    @Override
    public boolean supportsParameter(ParameterContext pc, ExtensionContext ec) {
        return pc.isAnnotated(Token.class) && pc.getParameter().getType().equals(String.class);
    }

    @Override
    public String resolveParameter(ParameterContext pc, ExtensionContext ec) {
        return TOKEN_HOLDER.get();
    }

    public static String getToken() {
        return TOKEN_HOLDER.get();
    }

    public static void setCode(String code) {
        CODE_HOLDER.set(code);
    }

    public static String getCode() {
        return CODE_HOLDER.get();
    }
}