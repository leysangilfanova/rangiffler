package com.leisan.rangiffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.value;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class MyProfilePage extends BasePage<MyProfilePage> {
  private final SelenideElement firstnameInput = $x("//input[@id='firstname']");
  private final SelenideElement surnameInput = $x("//input[@id='surname']");
  private final SelenideElement usernameInput = $x("//input[@id='username']");
  private final SelenideElement locationInput = $x("//div[@id='location']");
  public final SelenideElement avatar = $x("//div[contains(@class, 'MuiAvatar-root')]");
  private final SelenideElement saveButton = $x("//button[@type='submit']");
  private final SelenideElement avatarImageInput = $x("//input[@id='image__input']");
  private final SelenideElement successTitle = $x("//div[contains(text(), 'Your profile is successfully updated')]");
  private final SelenideElement errorFirstNameTitle = $("#firstname-helper-text");
  private final SelenideElement errorSurNameTitle = $("#surname-helper-text");

  @Nonnull
  @Step("Проверить видимость firstname")
  public MyProfilePage firstnameShouldBe(String expectedFirstname) {
    firstnameInput.shouldHave(value(expectedFirstname));
    return this;
  }

  @Nonnull
  @Step("Проверить видимость surname")
  public MyProfilePage surnameShouldBe(String expectedSurname) {
    surnameInput.shouldHave(value(expectedSurname));
    return this;
  }

  @Nonnull
  @Step("Проверить видимость username")
  public MyProfilePage usernameShouldBe(String expectedUsername) {
    usernameInput.shouldHave(value(expectedUsername));
    return this;
  }

  @Nonnull
  @Step("Проверить видимость location")
  public MyProfilePage locationNameShouldBe(String expectedLocationName) {
    locationInput.shouldHave(text(expectedLocationName));
    return this;
  }

  @Nonnull
  @Step("Установить firstName")
  public MyProfilePage setFirstname(String firstname) {
    firstnameInput.setValue(firstname);
    return this;
  }

  @Nonnull
  @Step("Установить surName")
  public MyProfilePage setSurname(String surname) {
    surnameInput.setValue(surname);
    return this;
  }

  @Nonnull
  @Step("Установить location")
  public MyProfilePage setLocation(String locationCode) {
    locationInput.click();
    $x("//li[@data-value='" + locationCode + "']").click();
    return this;
  }

  @Nonnull
  @Step("Установить avatar")
  public MyProfilePage setAvatar(String fileName) {
    avatarImageInput.uploadFromClasspath(fileName);
    return this;
  }

  @Nonnull
  @Step("Сохранить изменения")
  public MyProfilePage saveChanges() {
    saveButton.click();
    return this;
  }

  @Nonnull
  @Step("Проверить title обновления профиля")
  public MyProfilePage checkSuccessTitle(String value) {
    successTitle.shouldHave(text(value));
    return this;
  }

  @Nonnull
  @Step("Проверить ошибку firstname")
  public MyProfilePage checkErrorFirstnameTitle(String value) {
    errorFirstNameTitle.shouldHave(text(value));
    return this;
  }

  @Step("Проверить ошибку surname")
  public void checkErrorSurnameTitle(String value) {
    errorSurNameTitle.shouldHave(text(value));
  }
}
