package com.leisan.rangiffler.page.component;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$x;

public class AddNewPhotoForm extends BaseComponent<AddNewPhotoForm> {

  private final SelenideElement photoImageInput = self.$x(".//input[@id='image__input']");
  private final SelenideElement locationInput = self.$x(".//div[@id='country']");
  private final SelenideElement descriptionInput = self.$x(".//textarea[@id='description']");
  private final SelenideElement saveButton = self.$x(".//button[@type='submit']");

  public AddNewPhotoForm(SelenideElement self) {
    super(self);
  }

  public AddNewPhotoForm addPhoto(String fileName, String countryCode, String description) {
    photoImageInput.uploadFromClasspath(fileName);
    locationInput.click();
    $x("//li[@data-value='" + countryCode + "']").click();
    descriptionInput.setValue(description);
    saveButton.click();
    return this;
  }
}
