package com.leisan.rangiffler.gql.api;

import com.leisan.rangiffler.gql.FriendsQueryType;
import com.leisan.rangiffler.gql.FriendshipActionType;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class FriendsApi {

    @Step("Отправка запроса на получение списка друзей/заявок/людей")
    public Response executeFriendsQuery(FriendsQueryType queryType,
                                        String token,
                                        int page,
                                        int size,
                                        String searchQuery) {
        String queryTemplate = """
                query %s($page: Int, $size: Int, $searchQuery: String) {
                  user {
                    %s(page: $page, size: $size, searchQuery: $searchQuery) {
                      edges {
                        node {
                          id
                          username
                          firstname
                          surname
                          avatar
                          location {
                            code
                            name
                            flag
                            __typename
                          }
                          friendStatus
                          __typename
                        }
                        __typename
                      }
                      pageInfo {
                        hasPreviousPage
                        hasNextPage
                        __typename
                      }
                      __typename
                    }
                    __typename
                  }
                }
            """;

        // Для запроса PEOPLE структура немного отличается (используется users вместо user)
        if (queryType == FriendsQueryType.PEOPLE) {
            queryTemplate = """
                query %s($page: Int, $size: Int, $searchQuery: String) {
                  %s(page: $page, size: $size, searchQuery: $searchQuery) {
                    edges {
                      node {
                        id
                        username
                        firstname
                        surname
                        avatar
                        location {
                          code
                          name
                          flag
                          __typename
                        }
                        friendStatus
                        __typename
                      }
                      __typename
                    }
                    pageInfo {
                      hasPreviousPage
                      hasNextPage
                      __typename
                    }
                    __typename
                  }
                }
            """;
        }

        String query = String.format(queryTemplate,
                queryType.getOperationName(),
                queryType.getFieldName());

        Map<String, Object> variables = new HashMap<>();
        variables.put("page", page);
        variables.put("size", size);
        variables.put("searchQuery", searchQuery);

        return given()
                .spec(BaseApi.SPEC_GRAPHQL)
                .header("Authorization", "Bearer " + token)
                .body(Map.of(
                        "operationName", queryType.getOperationName(),
                        "query", query,
                        "variables", variables
                ))
                .post()
                .andReturn();
    }

    @Step("Выполнить действие с дружбой: {action}")
    public Response executeFriendshipAction(String token,
                                            String targetUserId,
                                            FriendshipActionType action) {
        String query = """
                mutation FriendshipAction($input: FriendshipInput!) {
                  friendship(input: $input) {
                    id
                    username
                    friendStatus
                    __typename
                  }
                }
                """;

        Map<String, Object> input = new HashMap<>();
        input.put("user", targetUserId);
        input.put("action", action.getAction());

        Map<String, Object> variables = new HashMap<>();
        variables.put("input", input);

        return given()
                .spec(BaseApi.SPEC_GRAPHQL)
                .header("Authorization", "Bearer " + token)
                .body(Map.of(
                        "operationName", "FriendshipAction",
                        "query", query,
                        "variables", variables
                ))
                .post()
                .andReturn();
    }
}
