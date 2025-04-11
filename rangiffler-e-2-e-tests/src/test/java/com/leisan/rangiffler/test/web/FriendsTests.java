package com.leisan.rangiffler.test.web;

import com.leisan.rangiffler.jupiter.annotation.ApiLogin;
import com.leisan.rangiffler.jupiter.annotation.CreateExtrasUsers;
import com.leisan.rangiffler.jupiter.annotation.CreateUser;
import com.leisan.rangiffler.jupiter.annotation.Extras;
import com.leisan.rangiffler.jupiter.annotation.Token;
import com.leisan.rangiffler.model.testdata.TestUser;
import com.leisan.rangiffler.service.impl.AuthApiClient;
import com.leisan.rangiffler.gql.FriendshipActionType;
import com.leisan.rangiffler.jupiter.annotation.meta.WebTest;
import com.leisan.rangiffler.service.impl.AuthClient;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.leisan.rangiffler.gql.api.BaseApi.friendSteps;
import static com.leisan.rangiffler.page.BasePage.mainPage;
import static io.qameta.allure.SeverityLevel.CRITICAL;
import static io.qameta.allure.SeverityLevel.NORMAL;

@Tag("web")
@Feature("Список друзей")
@WebTest
public class FriendsTests {
    AuthApiClient authApi = new AuthApiClient();

    @Test
    @ApiLogin
    @CreateUser
    @Severity(NORMAL)
    @Story("All people")
    @CreateExtrasUsers({@CreateUser, @CreateUser, @CreateUser})
    @DisplayName("Во вкладке 'All People' отображаются все пользователи")
    void allPeopleListTest() {
        mainPage.openPeoplesPage()
                .openAllPeopleTab()
                .usersCountShouldBeGreaterThan(3);
    }

    @Test
    @ApiLogin
    @CreateUser
    @Severity(NORMAL)
    @Story("All people")
    @CreateExtrasUsers({@CreateUser, @CreateUser})
    @DisplayName("Поиск во вкладке 'All People'")
    void allPeopleSearchTest(@Extras TestUser[] users) {
        mainPage.openPeoplesPage()
                .openAllPeopleTab()
                .usersCountShouldBeGreaterThan(2)
                .search(users[0].getUsername())
                .checkExistingFriends(users[0].getUsername())
                .usersCountShouldBeEqualTo(1);
    }

    @Test
    @ApiLogin
    @CreateUser
    @Severity(NORMAL)
    @Story("All people")
    @DisplayName("Во вкладках может не быть пользователей")
    void emptyListTest() {
        mainPage.openPeoplesPage()
                .noUserYetMessageShouldBePresented()
                .openOutcomeInvitationsTab()
                .noUserYetMessageShouldBePresented()
                .openIncomeInvitationsTab()
                .noUserYetMessageShouldBePresented();
    }

    @Test
    @ApiLogin
    @CreateUser
    @Severity(NORMAL)
    @Story("All people")
    @CreateExtrasUsers(@CreateUser)
    @DisplayName("Можно отправить заявку на добавление в друзья во вкладке 'All People'")
    void allPeopleAddToFriendListTest(@Extras TestUser[] users) {
        String username = users[0].getUsername();
        mainPage.openPeoplesPage()
                .openAllPeopleTab()
                .search(username)
                .shouldAddFriendButton()
                .addFriend(username)
                .shouldWaitingStatus();
    }

    @Test
    @ApiLogin
    @CreateUser
    @Severity(NORMAL)
    @Story("Outcome Invitations")
    @CreateExtrasUsers(@CreateUser)
    @DisplayName("Исходящая заявка в друзья отображается во вкладке 'Outcome Invitations'")
    void outcomeInvitationsListTest(@Extras TestUser[] users) {
        String userName = users[0].getUsername();
        mainPage.openPeoplesPage()
                .openAllPeopleTab()
                .search(userName)
                .shouldAddFriendButton()
                .addFriend(userName)
                .shouldWaitingStatus()
                .openOutcomeInvitationsTab()
                .checkExistingFriends(userName)
                .shouldWaitingStatus()
                .search(userName)
                .checkExistingFriends(userName)
                .shouldWaitingStatus();
    }

    @Test
    @ApiLogin
    @CreateUser
    @Severity(NORMAL)
    @Story("Outcome Invitations")
    @CreateExtrasUsers({@CreateUser, @CreateUser})
    @DisplayName("Поиск во вкладке 'Outcome Invitations'")
    void outcomeInvitationsSearchTest(@Extras TestUser[] users) {
        String friendName = users[0].getUsername();
        String secondFriendName = users[1].getUsername();

        mainPage.openPeoplesPage()
                .openAllPeopleTab()
                .search(friendName)
                .addFriend(friendName)
                .openFriendsTab()
                .openAllPeopleTab()
                .search(secondFriendName)
                .addFriend(secondFriendName)
                .openOutcomeInvitationsTab()
                .checkExistingFriends(friendName, secondFriendName)
                .usersCountShouldBeEqualTo(2)
                .search(friendName)
                .checkExistingFriends(friendName)
                .usersCountShouldBeEqualTo(1);
    }

    @Test
    @ApiLogin
    @CreateUser
    @Severity(NORMAL)
    @Story("Income Invitations")
    @CreateExtrasUsers(@CreateUser)
    @DisplayName("Входящая заявка в друзья отображается во вкладке 'Income Invitations'")
    void incomeInvitationsListTest(TestUser user, @Extras TestUser[] users, @AuthClient AuthApiClient client) {
        String friendName = users[0].getUsername();
        String extraUserToken = client.doLogin(users[0].getUsername(), users[0].getTestData().password());
        friendSteps.executeAction(extraUserToken, FriendshipActionType.ADD, user.getUsername());

        mainPage.openPeoplesPage()
                .openIncomeInvitationsTab()
                .checkExistingFriends(friendName)
                .acceptAndDeclineButtonsCheck(friendName)
                .search(friendName)
                .checkExistingFriends(friendName)
                .acceptAndDeclineButtonsCheck(friendName);
    }

    @Test
    @Severity(NORMAL)
    @CreateUser
    @ApiLogin
    @Story("Income Invitations")
    @CreateExtrasUsers(@CreateUser)
    @DisplayName("Прием заявки в друзья")
    void acceptInvitationToFriendsTest(TestUser user, @Extras TestUser[] users, @AuthClient AuthApiClient client) {
        String friendName = users[0].getUsername();
        String extraUserToken = client.doLogin(users[0].getUsername(), users[0].getTestData().password());
        friendSteps.executeAction(extraUserToken, FriendshipActionType.ADD, user.getUsername());

        mainPage.openPeoplesPage()
                .openIncomeInvitationsTab()
                .acceptInvitation(friendName)
                .addToFriendSuccessCheck()
                .shouldRemoveFriendButton()
                .openFriendsTab()
                .search(friendName)
                .checkExistingFriends(friendName)
                .shouldRemoveFriendButton()
                .openAllPeopleTab()
                .search(friendName)
                .shouldRemoveFriendButton()
                .checkExistingFriends(friendName);
    }

    @Test
    @ApiLogin
    @CreateUser
    @Severity(NORMAL)
    @Story("Income Invitations")
    @CreateExtrasUsers(@CreateUser)
    @DisplayName("Отклонение заявки в друзья")
    void declineInvitationToFriendsTest(TestUser user, @Extras TestUser[] users, @AuthClient AuthApiClient client) {
        String friendName = users[0].getUsername();
        String extraUserToken = client.doLogin(users[0].getUsername(), users[0].getTestData().password());
        friendSteps.executeAction(extraUserToken, FriendshipActionType.ADD, user.getUsername());

        mainPage.openPeoplesPage()
                .openIncomeInvitationsTab()
                .rejectInvitation(friendName)
                .invitationDeclineCheck()
                .refreshPage()
                .noUserYetMessageShouldBePresented()
                .search(friendName)
                .noUserYetMessageShouldBePresented();
    }

    @Test
    @ApiLogin
    @CreateUser
    @Severity(CRITICAL)
    @Story("Friends")
    @CreateExtrasUsers(@CreateUser)
    @DisplayName("Удаление друга")
    void deleteFriendTest(TestUser user, @Extras TestUser[] users, @AuthClient AuthApiClient client, @Token String token) {
        String friendName = users[0].getUsername();
        String extraUserToken = client.doLogin(users[0].getUsername(), users[0].getTestData().password());
        friendSteps.executeAction(extraUserToken, FriendshipActionType.ADD, user.getUsername());
        friendSteps.executeAction(token, FriendshipActionType.ACCEPT, users[0].getUsername());

        mainPage.openPeoplesPage()
                .openFriendsTab()
                .deleteFriend(friendName)
                .deleteFriendCheck()
                .refreshPage()
                .noUserYetMessageShouldBePresented()
                .search(friendName)
                .noUserYetMessageShouldBePresented();
    }

    @Test
    @ApiLogin
    @CreateUser
    @Story("Friends")
    @Severity(CRITICAL)
    @CreateExtrasUsers({@CreateUser, @CreateUser})
    @DisplayName("Во вкладке 'Friends' отображаются друзья")
    void friendsListTest(TestUser user, @Extras TestUser[] users, @AuthClient AuthApiClient client1, @AuthClient AuthApiClient client2, @Token String token) {
        String friendName1 = users[0].getUsername();
        String friendName2 = users[1].getUsername();
        String extraUserToken1 = client1.doLogin(users[0].getUsername(), users[0].getTestData().password());
        friendSteps.executeAction(extraUserToken1, FriendshipActionType.ADD, user.getUsername());
        String extraUserToken2 = client2.doLogin(users[1].getUsername(), users[1].getTestData().password());
        friendSteps.executeAction(extraUserToken2, FriendshipActionType.ADD, user.getUsername());

        friendSteps.executeAction(token, FriendshipActionType.ACCEPT, users[0].getUsername());
        friendSteps.executeAction(token, FriendshipActionType.ACCEPT, users[1].getUsername());

        mainPage.openPeoplesPage()
                .openFriendsTab()
                .checkExistingFriends(friendName1, friendName2)
                .shouldRemoveFriendButton();
    }

    @Test
    @ApiLogin
    @CreateUser
    @Severity(NORMAL)
    @Story("Friends")
    @CreateExtrasUsers({@CreateUser, @CreateUser})
    @DisplayName("Поиск во вкладке 'Friends'")
    void friendsSearchTest(TestUser user, @Extras TestUser[] users, @AuthClient AuthApiClient client1, @AuthClient AuthApiClient client2, @Token String token) {
        String friendName1 = users[0].getUsername();
        String friendName2 = users[1].getUsername();
        String extraUserToken1 = client1.doLogin(users[0].getUsername(), users[0].getTestData().password());
        friendSteps.executeAction(extraUserToken1, FriendshipActionType.ADD, user.getUsername());
        String extraUserToken2 = client2.doLogin(users[1].getUsername(), users[1].getTestData().password());
        friendSteps.executeAction(extraUserToken2, FriendshipActionType.ADD, user.getUsername());

        friendSteps.executeAction(token, FriendshipActionType.ACCEPT, users[0].getUsername());
        friendSteps.executeAction(token, FriendshipActionType.ACCEPT, users[1].getUsername());

        mainPage.openPeoplesPage()
                .openFriendsTab()
                .search(friendName1)
                .checkExistingFriends(friendName1)
                .refreshPage()
                .shouldRemoveFriendButton()
                .search(friendName2)
                .checkExistingFriends(friendName2);
    }
}