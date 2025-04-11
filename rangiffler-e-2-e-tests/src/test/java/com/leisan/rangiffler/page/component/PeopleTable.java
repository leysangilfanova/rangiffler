package com.leisan.rangiffler.page.component;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.$x;


public class PeopleTable extends BaseComponent<PeopleTable> {

  public PeopleTable(SelenideElement self) {
    super(self);
  }

  public ElementsCollection getAllRows() {
    return $$x("//tbody/tr");
  }

  public SelenideElement getRowByUsername(String username) {
    var allRows = getAllRows();
    var table = $x("//table");
    table.shouldBe(Condition.visible);
    return allRows.find(text(username));
  }

  public PeopleTable addFriend(String username) {
    var user = getRowByUsername(username);
    user.$x(".//button[text() = 'Add']").click();
    return this;
  }

  public PeopleTable acceptInvitation(String username) {
    var user = getRowByUsername(username);
    user.$x(".//button[text() = 'Accept']").click();
    return this;
  }

  public PeopleTable acceptAndDeclineButtonsCheck(String username) {
    var user = getRowByUsername(username);
    user.$x(".//button[text() = 'Accept']").shouldBe(visible);
    user.$x(".//button[text() = 'Decline']").shouldBe(visible);
    return this;
  }

  public PeopleTable rejectInvitation(String username) {
    var user = getRowByUsername(username);
    user.$x(".//button[text() = 'Decline']").click();
    return this;
  }

  public PeopleTable deleteFriend(String username) {
    var user = getRowByUsername(username);
    user.$x(".//button[text() = 'Remove']").click();
    return this;
  }

  public PeopleTable usersCountShouldBeGreaterThan(int expectedCount) {
    getAllRows().shouldHave(sizeGreaterThan(expectedCount));
    return this;
  }

  public PeopleTable usersCountShouldBeEqualTo(int expectedCount) {
    getAllRows().shouldHave(size(expectedCount));
    return this;
  }
}
