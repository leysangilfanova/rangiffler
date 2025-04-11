package com.leisan.rangiffler.gql.api;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class UserApi {

    @Step("Обновить данные пользователя")
    public Response updateUser(String token,
                               String firstname,
                               String surname,
                               String avatar,
                               String countryCode) {
        String query = """
            mutation UpdateUser($input: UserInput!) {
              user(input: $input) {
                id
                username
                firstname
                surname
                avatar
                location {
                  code
                  name
                  __typename
                }
                __typename
              }
            }
            """;

        Map<String, Object> input = new HashMap<>();
        input.put("firstname", firstname);
        input.put("surname", surname);
        input.put("avatar", avatar);
        input.put("location", countryCode);

        Map<String, Object> body = Map.of(
                "operationName", "UpdateUser",
                "query", query,
                "variables", Map.of("input", input)
        );

        return given()
                .spec(BaseApi.SPEC_GRAPHQL)
                .header("Authorization", token)
                .body(body)
                .post()
                .andReturn();
    }

    @Step("Получить данные текущего пользователя")
    public Response getUser(String token) {
        String query = """
            query GetUser {
              user {
                id
                username
                firstname
                surname
                avatar
                location {
                  code
                  name
                  __typename
                }
                __typename
              }
            }
            """;

        Map<String, Object> body = Map.of(
                "operationName", "GetUser",
                "query", query,
                "variables", Map.of()
        );

        return given()
                .spec(BaseApi.SPEC_GRAPHQL)
                .header("Authorization", "Bearer " + token)
                .body(body)
                .post()
                .andReturn();
    }
}
