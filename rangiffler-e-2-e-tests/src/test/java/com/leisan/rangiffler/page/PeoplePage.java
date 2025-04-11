package com.leisan.rangiffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.leisan.rangiffler.config.Config;
import io.qameta.allure.Step;
import com.leisan.rangiffler.page.component.PeopleTable;

import javax.annotation.Nonnull;

import static com.codeborne.selenide.CollectionCondition.textsInAnyOrder;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.clearBrowserCookies;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.refresh;
import static com.codeborne.selenide.Selenide.sleep;

public class PeoplePage extends BasePage<PeoplePage> {

    private static final Config CFG = Config.getInstance();
    private final SelenideElement friendsTab = $x("//button[text()='Friends']");
    private final SelenideElement incomeInvitationsTab = $x("//button[text()='Income invitations']");
    private final SelenideElement outcomeInvitationsTab = $x("//button[text()='Outcome invitations']");
    private final SelenideElement allPeopleTab = $x("//button[text()='All People']");
    private final SelenideElement searchInput = $x("//input[@placeholder='Search people']");
    private final SelenideElement noUserYetMessage = $x("//p[text()='There are no users yet']");
    private final SelenideElement removeFriendButton = $x("//button[text()='Remove']");
    private final SelenideElement addFriendButton = $x("//button[text()='Add']");
    private final SelenideElement waitingStatus = $x("//span[text()='Waiting...']");
    private final SelenideElement popup = $("[role='presentation'] [role='alert']");
    private final PeopleTable table = new PeopleTable($("//table"));
    private final SelenideElement logoutButton = $("[data-testid=\"ExitToAppOutlinedIcon\"]");
    private final SelenideElement travelsButton = $("[href=\"/my-travels\"]");

    public ElementsCollection getAllRows() {
        return $$x("//tbody/tr");
    }

    @Nonnull
    @Step("Открыть таб Friends")
    public PeoplePage openFriendsTab() {
        friendsTab.click();
        return this;
    }

    @Nonnull
    @Step("Отобразился попап о добавлении друга в друзья")
    public PeoplePage addToFriendSuccessCheck() {
        popup.shouldBe(visible).shouldHave(text("Invitation accepted"));
        return this;
    }

    @Nonnull
    @Step("Отобразился попап об отклонении заявки в друзья")
    public PeoplePage invitationDeclineCheck() {
        popup.shouldBe(visible).shouldHave(text("Invitation declined"));
        return this;
    }

    @Nonnull
    @Step("Отобразился попап об удалении друга")
    public PeoplePage deleteFriendCheck() {
        popup.shouldBe(visible).shouldHave(text("Friend deleted"));
        return this;
    }

    @Nonnull
    @Step("Открыть таб IncomeInvitations")
    public PeoplePage openIncomeInvitationsTab() {
        incomeInvitationsTab.click();
        return this;
    }

    @Nonnull
    @Step("Проверить видимость кнопки Добавить в друзья")
    public PeoplePage shouldAddFriendButton() {
        addFriendButton.shouldBe(visible);
        return this;
    }

    @Nonnull
    @Step("Проверить видимость кнопки Удалить из друзей")
    public PeoplePage shouldRemoveFriendButton() {
        removeFriendButton.shouldBe(visible);
        return this;
    }

    @Nonnull
    @Step("Проверить отображения статуса Waiting")
    public PeoplePage shouldWaitingStatus() {
        waitingStatus.shouldBe(visible);
        return this;
    }

    @Nonnull
    @Step("Открыть таб OutcomeInvitations")
    public PeoplePage openOutcomeInvitationsTab() {
        outcomeInvitationsTab.click();
        return this;
    }

    @Nonnull
    @Step("Открыть таб AllPeople")
    public PeoplePage openAllPeopleTab() {
        allPeopleTab.click();
        return this;
    }

    @Nonnull
    @Step("Обновить страницу")
    public PeoplePage refreshPage() {
        refresh();
        return this;
    }

    @Nonnull
    @Step("Проверить видимость сообщения заглушки")
    public PeoplePage noUserYetMessageShouldBePresented() {
        noUserYetMessage.shouldBe(visible);
        return this;
    }

    @Nonnull
    @Step("Ввести в поиск значение: {0}")
    public PeoplePage search(String searchQuery) {
        searchInput.setValue(searchQuery).pressEnter();
        return this;
    }

    @Nonnull
    @Step("Открыть страницу путешествий")
    public PeoplePage openTravelsPage() {
        travelsButton.click();
        return this;
    }

    @Nonnull
    @Step("Добавить друга")
    public PeoplePage addFriend(String username) {
        table.addFriend(username);
        return this;
    }

    @Nonnull
    @Step("Принять приглашение")
    public PeoplePage acceptInvitation(String username) {
        table.acceptInvitation(username);
        return this;
    }

    @Nonnull
    @Step("Проверка наличия кнопок 'Accept' и 'Decline'")
    public PeoplePage acceptAndDeclineButtonsCheck(String username) {
        table.acceptAndDeclineButtonsCheck(username);
        return this;
    }

    @Nonnull
    @Step("Отклонить приглашение")
    public PeoplePage rejectInvitation(String username) {
        table.rejectInvitation(username);
        return this;
    }

    @Nonnull
    @Step("Удалить друга")
    public PeoplePage deleteFriend(String username) {
        table.deleteFriend(username);
        return this;
    }

    @Nonnull
    @Step("Проверить что отображаются ожидаемые друзья")
    public PeoplePage checkExistingFriends(String... expectedUsernames) {
        getAllRows().shouldHave(textsInAnyOrder(expectedUsernames));
        return this;
    }

    @Nonnull
    @Step("Проверить представление пользователей на странице")
    public PeoplePage usersCountShouldBeGreaterThan(int expectedCount) {
        table.usersCountShouldBeGreaterThan(expectedCount);
        return this;
    }

    @Nonnull
    @Step("Проверить представление пользователей на странице")
    public PeoplePage usersCountShouldBeEqualTo(int expectedCount) {
        table.usersCountShouldBeEqualTo(expectedCount);
        return this;
    }

    @Step("Разлогиниться")
    @Nonnull
    public PeoplePage signOut() {
        sleep(2000);
        logoutButton.click();
        clearBrowserCookies();
        return new PeoplePage();
    }
}
