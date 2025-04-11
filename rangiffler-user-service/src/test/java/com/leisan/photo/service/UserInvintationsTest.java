package com.leisan.photo.service;

import com.leisan.user.service.graphql.dto.UserConnectionGql;
import com.leisan.user.service.graphql.dto.UserGql;
import com.leisan.user.service.model.FriendStatus;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class UserInvintationsTest extends BaseTest {

    @Test
    void notFriendSuccess() {
        var currentUser = "currentUser";
        var toFriendshipUser = "toFriendshipUser";
        createUser(currentUser);
        createUser(toFriendshipUser);
        var status = friendshipService.friendStatus(currentUser, toFriendshipUser);
        assertThat(status).isNull();
        var friends = userService.friends(0, 1, null, currentUser);
        assertThat(friends)
                .isNotNull()
                .returns(0, UserConnectionGql::total);
    }

    @Test
    void addToFriendAcceptSuccess() {
        var currentUser = "currentUser";
        var toFriendshipUser = "toFriendshipUser";
        Mockito.when(authService.getCurrentUsername())
                .thenReturn(currentUser)
                .thenReturn(toFriendshipUser)
                .thenReturn(currentUser);
        createUser(currentUser);
        createUser(toFriendshipUser);

        friendshipService.createFriendshipRequest(currentUser, toFriendshipUser);
        friendshipService.acceptFriendshipRequest(toFriendshipUser, currentUser);
        var status = friendshipService.friendStatus(currentUser, toFriendshipUser);
        assertThat(status).isNotNull().isEqualTo(FriendStatus.FRIEND);
        var friends = userService.friends(0, 1, null, currentUser);
        assertThat(friends)
                .isNotNull()
                .returns(1, UserConnectionGql::total)
                .extracting(u -> u.edges().get(0).node())
                .returns(toFriendshipUser, UserGql::getUsername);
        var friends2 = userService.friends(0, 1, null, toFriendshipUser);
        assertThat(friends2)
                .isNotNull()
                .returns(1, UserConnectionGql::total)
                .extracting(u -> u.edges().get(0).node())
                .returns(currentUser, UserGql::getUsername);
    }

    @Test
    void addToFriendAcceptDeleteSuccess() {
        var currentUser = "currentUser";
        var toFriendshipUser = "toFriendshipUser";
        Mockito.when(authService.getCurrentUsername())
                .thenReturn(currentUser)
                .thenReturn(toFriendshipUser)
                .thenReturn(currentUser);
        createUser(currentUser);
        createUser(toFriendshipUser);

        friendshipService.createFriendshipRequest(currentUser, toFriendshipUser);
        friendshipService.acceptFriendshipRequest(toFriendshipUser, currentUser);
        var status = friendshipService.friendStatus(currentUser, toFriendshipUser);
        assertThat(status).isNotNull().isEqualTo(FriendStatus.FRIEND);
        friendshipService.removeFriend(currentUser, toFriendshipUser);
        var status2 = friendshipService.friendStatus(currentUser, toFriendshipUser);
        assertThat(status2).isNull();
    }

    @Test
    void addToFriendAcceptDeclineSuccess() {
        var currentUser = "currentUser";
        var toFriendshipUser = "toFriendshipUser";
        Mockito.when(authService.getCurrentUsername())
                .thenReturn(currentUser)
                .thenReturn(toFriendshipUser)
                .thenReturn(currentUser);
        createUser(currentUser);
        createUser(toFriendshipUser);

        friendshipService.createFriendshipRequest(currentUser, toFriendshipUser);
        friendshipService.declineFriendshipRequest(toFriendshipUser, currentUser);
        var status = friendshipService.friendStatus(currentUser, toFriendshipUser);
        assertThat(status).isNull();
    }

    @Test
    void addToFriendHasIncomeSuccess() {
        var currentUser = "currentUser";
        var toFriendshipUser = "toFriendshipUser";
        Mockito.when(authService.getCurrentUsername())
                .thenReturn(currentUser)
                .thenReturn(toFriendshipUser)
                .thenReturn(currentUser);
        createUser(currentUser);
        createUser(toFriendshipUser);

        friendshipService.createFriendshipRequest(currentUser, toFriendshipUser);
        var status = friendshipService.friendStatus(currentUser, toFriendshipUser);
        assertThat(status).isNotNull().isEqualTo(FriendStatus.INVITATION_SENT);
        var income = userService.incomeInvitations(toFriendshipUser, null, 0, 10);
        assertThat(income)
                .isNotNull()
                .returns(1, UserConnectionGql::total)
                .extracting(u -> u.edges().get(0).node())
                .returns(currentUser, UserGql::getUsername);
    }

    @Test
    void addToFriendHasOutcomeSuccess() {
        var currentUser = "currentUser";
        var toFriendshipUser = "toFriendshipUser";
        Mockito.when(authService.getCurrentUsername())
                .thenReturn(currentUser)
                .thenReturn(toFriendshipUser)
                .thenReturn(currentUser);
        createUser(currentUser);
        createUser(toFriendshipUser);

        friendshipService.createFriendshipRequest(currentUser, toFriendshipUser);
        var status = friendshipService.friendStatus(toFriendshipUser, currentUser);
        assertThat(status).isNotNull().isEqualTo(FriendStatus.INVITATION_RECEIVED);
        var outcome = userService.outcomeInvitations(currentUser, null, 0, 10);
        assertThat(outcome)
                .isNotNull()
                .returns(1, UserConnectionGql::total)
                .extracting(u -> u.edges().get(0).node())
                .returns(toFriendshipUser, UserGql::getUsername);
    }

    private void createUser(String username) {
        var value = String.format("""
                {
                  "username": "%s"
                }
                """, username);
        userListener.receive(new ConsumerRecord<>("users", 1, 1, username, value));
    }
}
