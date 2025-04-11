package com.leisan.photo.service;

import com.leisan.graphql.common.test.GraphqlAssertions;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Map;

class UsersResolverTest extends BaseTest {

    @Test
    void addToFriendSuccess() {
        var currentUser = "currentUser";
        Mockito.when(authService.getCurrentUsername()).thenReturn(currentUser);
        createUser(currentUser);
        for (int i = 0; i < 10; i++) {
            createUser("test_user_" + i);
        }

        var query = """
                query Query($page: Int, $size: Int, $searchQuery: String) {
                   users(page: $page, size: $size, searchQuery: $searchQuery) {
                     edges {
                       node {
                         firstname
                         id
                         surname
                         username
                         avatar
                         friendStatus
                         location {
                           code
                         }
                       }
                     }
                     total
                   }
                 }
                """;
        Map<String, Object> variablesMap = Map.of(
                "page", 0,
                "size", 5,
                "searchQuery", "test_user"
        );
        var firstPageRs = graphqlClient.makeGraphqlCall(
                GraphqlRq.builder()
                        .operationName("Query")
                        .query(query)
                        .variables(variablesMap)
                        .build()
        );
        GraphqlAssertions.assertGraphqlRs(firstPageRs)
                .isNotNull()
                .assertEqualsByJsonPath("users.total", 10);
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
