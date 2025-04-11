package com.leisan.rangiffler.page;

import com.codeborne.selenide.SelenideElement;
import com.leisan.rangiffler.config.Config;
import io.qameta.allure.Step;
import com.leisan.rangiffler.page.component.AddNewPhotoForm;

import javax.annotation.Nonnull;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;
import static java.util.Objects.requireNonNull;

public class MyTravelsPage extends BasePage<MyTravelsPage> {

  private static final Config CFG = Config.getInstance();
  public static final String URL = CFG.redirectUrl();
  private final SelenideElement travelsTabButton = $("[href=\"/my-travels\"]");
  private final SelenideElement descriptionInput = $("#description");
  private final SelenideElement likeButton = $x("//button[@aria-label=\"like\"]");
  private final SelenideElement withFriendsButton = $x("//button[text()='With friends']");
  private final SelenideElement likeAlert = $x("//div[text()='Post was succesfully liked']");
  private final SelenideElement addPhotoButton = $x("//button[text()='Add photo']");
  private final SelenideElement saveButton = $x("//button[text()='Save']");
  private final SelenideElement deletePostAlert = $x("//div[text()='Post deleted']");
  private final SelenideElement deletePhotoButton = $x("//button[text()='Delete']");
  private final SelenideElement editPhotoButton = $(byText("Edit"));
  private final SelenideElement uploadNewPhotoButton = $(byText("Upload new image"));
  private final SelenideElement imageRequiredError = $x("//div[text()='Please upload an image']");
  private final AddNewPhotoForm addNewPhotoForm = new AddNewPhotoForm($x("//form[contains(@class, 'MuiGrid-root')]"));
  public final SelenideElement worldMap = $x("//figure[contains(@class, 'worldmap__figure-container')]");
  public final SelenideElement image = $("img.image-upload__image");
  private final SelenideElement photoImageInput = $x(".//input[@id='image__input']");
  SelenideElement likeCount = $x("//button[@aria-label='like']/preceding-sibling::p");
  private final SelenideElement popup = $("[role='presentation'] [role='alert']");

  @Nonnull
  @Step("Открыть страницу с фото")
  public MyTravelsPage openTravelsPage() {
    travelsTabButton.click();
    return this;
  }

  @Nonnull
  @Step("Поставить лайк")
  public MyTravelsPage clickLikeButton() {
    likeButton.click();
    return this;
  }

  @Nonnull
  @Step("Отобразился попап об успешном создании поста")
  public MyTravelsPage postCreatedCheck() {
    popup.shouldBe(visible).shouldHave(text("New post created"));
    return this;
  }

  @Nonnull
  @Step("Установить description")
  public MyTravelsPage setDescription(String value) {
    descriptionInput.setValue(value);
    return this;
  }

  @Nonnull
  @Step("Проверить текст удаления фотокарточки")
  public MyTravelsPage shouldDeletePostAlert(String value) {
    deletePostAlert.shouldHave(text(value));
    return this;
  }

  @Nonnull
  @Step("Нажать на кнопку удаления фотокарточки")
  public MyTravelsPage clickDeleteCardButton() {
    deletePhotoButton.click();
    return this;
  }

  @Nonnull
  @Step("Проверить количество лайков")
  public MyTravelsPage checkLikeCount(String value) {
    likeCount.shouldHave(text(value));
    return this;
  }

  @Nonnull
  @Step("Проверить текст установки лайка")
  public MyTravelsPage checkLikeAlert(String value) {
    likeAlert.shouldHave(text(value));
    return this;
  }

  @Nonnull
  @Step("Нажать на фильтр С Друзьями")
  public MyTravelsPage clickWithFriendsButton() {
    withFriendsButton.click();
    return this;
  }

  @Nonnull
  @Step("Добавить фотокарточку")
  public MyTravelsPage addPhoto(String fileName, String countryCode, String description) {
    addPhotoButton.click();
    addNewPhotoForm.addPhoto(fileName, countryCode, description);
    return this;
  }

  @Nonnull
  @Step("Изменить фотокарточку")
  public MyTravelsPage editPhoto(String fileName, String countryCode, String description) {
    editPhotoButton.click();
    addNewPhotoForm.addPhoto(fileName, countryCode, description);
    return this;
  }

  @Nonnull
  @Step("Изменить фотокарточку")
  public MyTravelsPage editPhoto(String fileName) {
    editPhotoButton.click();
    uploadNewPhotoButton.click();
    photoImageInput.uploadFromClasspath(fileName);
    return this;
  }

  @Nonnull
  @Step("Нажать на кнопку добавления фотокарточки")
  public MyTravelsPage clickAddPhotoButton() {
    addPhotoButton.click();
    return this;
  }

  @Nonnull
  @Step("Нажать на кнопку сохранения фотокарточки")
  public MyTravelsPage clickSavePhotoButton() {
    saveButton.click();
    return this;
  }

  @Nonnull
  @Step("Проверить текст ошибки обязательности фотокарточки")
  public MyTravelsPage checkPhotoError(String value) {
    imageRequiredError.shouldHave(text(value));
    return this;
  }

  @Nonnull
  @Step("Сделать скриншот элемента")
  public BufferedImage makeScreenshot(File input) throws IOException {
    return ImageIO.read(requireNonNull(input));
  }
}
