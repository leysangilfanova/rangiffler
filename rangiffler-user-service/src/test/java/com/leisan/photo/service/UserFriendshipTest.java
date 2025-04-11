package com.leisan.photo.service;

import com.leisan.graphql.common.test.GraphqlAssertions;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Map;

class UserFriendshipTest extends BaseTest {

    @Test
    void addToFriendSuccess() {
        var currentUser = "currentUser";
        var toFriendshipUser = "toFriendshipUser";
        Mockito.when(authService.getCurrentUsername()).thenReturn(currentUser);
        createUser(currentUser);
        createUser(toFriendshipUser);

        var query = """
                mutation Mutation($input: FriendshipInput!) {
                  friendship(input: $input) {
                    firstname
                    username
                    surname
                    friendStatus
                  }
                }
                """;
        Map<String, Object> variablesMap = Map.of(
                "input", Map.of(
                        "action", "ADD",
                        "user", toFriendshipUser
                )
        );
        var friendshipRs = graphqlClient.makeGraphqlCall(
                GraphqlRq.builder()
                        .operationName("Mutation")
                        .query(query)
                        .variables(variablesMap)
                        .build()
        );
        GraphqlAssertions.assertGraphqlRs(friendshipRs)
                .isNotNull()
                .assertEqualsByJsonPath("friendship.username", toFriendshipUser)
                .assertEqualsByJsonPath("friendship.friendStatus", "INVITATION_SENT");
        ;
    }

    @Test
    void addToFriendAcceptSuccess() {
        var currentUser = "currentUser";
        var toFriendshipUser = "toFriendshipUser";
        Mockito.when(authService.getCurrentUsername()).thenReturn(currentUser).thenReturn(toFriendshipUser);
        createUser(currentUser);
        createUser(toFriendshipUser);

        var query = """
                mutation Mutation($input: FriendshipInput!) {
                  friendship(input: $input) {
                    firstname
                    username
                    surname
                    friendStatus
                  }
                }
                """;
        Map<String, Object> variablesMap = Map.of(
                "input", Map.of(
                        "action", "ADD",
                        "user", toFriendshipUser
                )
        );
        var friendshipRs = graphqlClient.makeGraphqlCall(
                GraphqlRq.builder()
                        .operationName("Mutation")
                        .query(query)
                        .variables(variablesMap)
                        .build()
        );

        var queryAccept = """
                mutation Mutation($input: FriendshipInput!) {
                  friendship(input: $input) {
                    firstname
                    username
                    surname
                    friendStatus
                  }
                }
                """;
        Map<String, Object> variablesMapAccept = Map.of(
                "input", Map.of(
                        "action", "ACCEPT",
                        "user", currentUser
                )
        );
        var friendshipAcceptRs = graphqlClient.makeGraphqlCall(
                GraphqlRq.builder()
                        .operationName("Mutation")
                        .query(queryAccept)
                        .variables(variablesMapAccept)
                        .build()
        );
        GraphqlAssertions.assertGraphqlRs(friendshipAcceptRs)
                .isNotNull()
                .assertEqualsByJsonPath("friendship.username", currentUser)
                .assertEqualsByJsonPath("friendship.friendStatus", "FRIEND");
    }

    @Test
    void addToFriendDeclineSuccess() {
        var currentUser = "currentUser";
        var toFriendshipUser = "toFriendshipUser";
        Mockito.when(authService.getCurrentUsername()).thenReturn(currentUser).thenReturn(toFriendshipUser);
        createUser(currentUser);
        createUser(toFriendshipUser);

        var query = """
                mutation Mutation($input: FriendshipInput!) {
                  friendship(input: $input) {
                    firstname
                    username
                    surname
                    friendStatus
                  }
                }
                """;
        Map<String, Object> variablesMap = Map.of(
                "input", Map.of(
                        "action", "ADD",
                        "user", toFriendshipUser
                )
        );
        var friendshipRs = graphqlClient.makeGraphqlCall(
                GraphqlRq.builder()
                        .operationName("Mutation")
                        .query(query)
                        .variables(variablesMap)
                        .build()
        );

        var queryAccept = """
                mutation Mutation($input: FriendshipInput!) {
                  friendship(input: $input) {
                    firstname
                    username
                    surname
                    friendStatus
                  }
                }
                """;
        Map<String, Object> variablesMapAccept = Map.of(
                "input", Map.of(
                        "action", "REJECT",
                        "user", currentUser
                )
        );
        var friendshipAcceptRs = graphqlClient.makeGraphqlCall(
                GraphqlRq.builder()
                        .operationName("Mutation")
                        .query(queryAccept)
                        .variables(variablesMapAccept)
                        .build()
        );
        GraphqlAssertions.assertGraphqlRs(friendshipAcceptRs)
                .isNotNull()
                .assertEqualsByJsonPath("friendship.username", currentUser)
                .assertEqualsByJsonPath("friendship.friendStatus", null);
    }

    @Test
    void addToFriendAcceptedAndDeleteSuccess() {
        var currentUser = "currentUser";
        var toFriendshipUser = "toFriendshipUser";
        Mockito.when(authService.getCurrentUsername()).thenReturn(currentUser).thenReturn(toFriendshipUser);
        createUser(currentUser);
        createUser(toFriendshipUser);

        var query = """
                mutation Mutation($input: FriendshipInput!) {
                  friendship(input: $input) {
                    firstname
                    username
                    surname
                    friendStatus
                  }
                }
                """;
        Map<String, Object> variablesMap = Map.of(
                "input", Map.of(
                        "action", "ADD",
                        "user", toFriendshipUser
                )
        );
        var friendshipRs = graphqlClient.makeGraphqlCall(
                GraphqlRq.builder()
                        .operationName("Mutation")
                        .query(query)
                        .variables(variablesMap)
                        .build()
        );

        var queryAccept = """
                mutation Mutation($input: FriendshipInput!) {
                  friendship(input: $input) {
                    firstname
                    username
                    surname
                    friendStatus
                  }
                }
                """;
        Map<String, Object> variablesMapAccept = Map.of(
                "input", Map.of(
                        "action", "ACCEPT",
                        "user", currentUser
                )
        );
        var friendshipAcceptRs = graphqlClient.makeGraphqlCall(
                GraphqlRq.builder()
                        .operationName("Mutation")
                        .query(queryAccept)
                        .variables(variablesMapAccept)
                        .build()
        );

        var queryDelete = """
                mutation Mutation($input: FriendshipInput!) {
                  friendship(input: $input) {
                    firstname
                    username
                    surname
                    friendStatus
                  }
                }
                """;
        Map<String, Object> variablesMapDelete = Map.of(
                "input", Map.of(
                        "action", "DELETE",
                        "user", currentUser
                )
        );
        var friendshipDeleteRs = graphqlClient.makeGraphqlCall(
                GraphqlRq.builder()
                        .operationName("Mutation")
                        .query(queryDelete)
                        .variables(variablesMapDelete)
                        .build()
        );
        GraphqlAssertions.assertGraphqlRs(friendshipDeleteRs)
                .isNotNull()
                .assertEqualsByJsonPath("friendship.username", currentUser)
                .assertEqualsByJsonPath("friendship.friendStatus", null);
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
