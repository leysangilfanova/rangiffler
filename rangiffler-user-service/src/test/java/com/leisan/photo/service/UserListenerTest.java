package com.leisan.photo.service;

import com.leisan.graphql.common.test.GraphqlAssertions;
import com.leisan.user.service.model.User;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class UserListenerTest extends BaseTest {

    @Test
    void consumeKafkaEventUserSaved() {
        var username = "test_username";
        var value = """
                {
                  "username": "test_username"
                }
                """;
        userListener.receive(new ConsumerRecord<>("users", 1, 1, username, value));

        var user = userRepository.find(username).orElse(null);
        assertThat(user)
                .isNotNull()
                .returns(username, User::getUsername);
    }

    @Test
    void consumeKafkaEventUserSavedGqlSuccess() {
        var username = "test_username";
        var value = """
                {
                  "username": "test_username"
                }
                """;

        var query = """
                query Query {
                  user {
                    firstname
                    avatar
                    friendStatus
                    location {
                      code
                    }
                    id
                    surname
                    username
                  }
                }
                """;
        userListener.receive(new ConsumerRecord<>("users", 1, 1, username, value));
        Mockito.when(authService.getCurrentUsername()).thenReturn(username);

        var userRs = graphqlClient.makeGraphqlCall(GraphqlRq.builder()
                .operationName("Query")
                .query(query)
                .variables(Map.of())
                .build());

        GraphqlAssertions.assertGraphqlRs(userRs)
                .assertEqualsByJsonPath("user.username", username);
    }
}
