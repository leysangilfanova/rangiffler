package com.leisan.rangiffler.page;

import com.codeborne.selenide.SelenideElement;
import com.leisan.rangiffler.config.Config;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class MainPage extends BasePage<MainPage> {
    private static final Config CFG = Config.getInstance();
    public static final String URL = CFG.frontUrl() + "/my-travels";
    private final SelenideElement peopleTabButton = $("[href=\"/people\"]");
    private final SelenideElement profileTabButton = $("[href=\"/profile\"]");

    @Step("Проверить загрузку страницы")
    public void checkThatHeaderPageLoaded() {
        peopleTabButton.should(visible);
        profileTabButton.shouldBe(visible);
    }

    @Nonnull
    @Step("Открыть страницу People")
    public PeoplePage openPeoplesPage() {
        peopleTabButton.click();
        return new PeoplePage();
    }

    @Nonnull
    @Step("Открыть страницу Profile")
    public MyProfilePage openProfilePage() {
        profileTabButton.click();
        return new MyProfilePage();
    }
}
