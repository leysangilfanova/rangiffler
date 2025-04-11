package com.leisan.rangiffler.gql.api;

import com.leisan.rangiffler.utils.FileUtils;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class TravelsApi {

    @Step("Получить список стран")
    public Response getCountries(String token) {
        String query = """
            query GetCountries {
              countries {
                code
                name
                flag
                __typename
              }
            }
            """;

        return given()
                .spec(BaseApi.SPEC_GRAPHQL)
                .header("Authorization", "Bearer " + token)
                .body(Map.of(
                        "operationName", "GetCountries",
                        "query", query,
                        "variables", new HashMap<>()
                ))
                .post()
                .andReturn();
    }

    @Step("Получить ленту без друзей")
    public Response getFeedWithoutFriends(String token, int page, int size) {
        String query = """
            query GetFeed($page: Int, $size: Int, $withFriends: Boolean!) {
              feed(withFriends: $withFriends) {
                photos(page: $page, size: $size) {
                  edges {
                    node {
                      id
                      src
                      country {
                        code
                        name
                        flag
                        __typename
                      }
                      description
                      likes {
                        total
                        likes {
                          user
                          __typename
                        }
                        __typename
                      }
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
                stat {
                  count
                  country {
                    code
                    __typename
                  }
                  __typename
                }
                __typename
              }
            }
            """;

        Map<String, Object> variables = Map.of(
                "withFriends", false,
                "page", page,
                "size", size
        );

        return given()
                .spec(BaseApi.SPEC_GRAPHQL)
                .header("Authorization", "Bearer " + token)
                .body(Map.of(
                        "operationName", "GetFeed",
                        "query", query,
                        "variables", variables
                ))
                .post()
                .andReturn();
    }


    @Step("Получить ленту с друзьями")
    public Response getFeedWithFriends(String token, int page, int size) {
        String query = """
            query GetFeed($page: Int, $size: Int, $withFriends: Boolean!) {
              feed(withFriends: $withFriends) {
                photos(page: $page, size: $size) {
                  edges {
                    node {
                      id
                      src
                      country {
                        code
                        name
                        flag
                        __typename
                      }
                      description
                      likes {
                        total
                        likes {
                          user
                          __typename
                        }
                        __typename
                      }
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
                stat {
                  count
                  country {
                    code
                    __typename
                  }
                  __typename
                }
                __typename
              }
            }
            """;

        Map<String, Object> variables = Map.of(
                "withFriends", true,
                "page", page,
                "size", size
        );

        return given()
                .spec(BaseApi.SPEC_GRAPHQL)
                .header("Authorization", "Bearer " + token)
                .body(Map.of(
                        "operationName", "GetFeed",
                        "query", query,
                        "variables", variables
                ))
                .post()
                .andReturn();
    }

    @Step("Создать фото")
    public Response createPhoto(String token, String photo, String description, String countryCode) {
        String query = """
            mutation CreatePhoto($input: PhotoInput!) {
              photo(input: $input) {
                id
                src
                country {
                  code
                  name
                  flag
                  __typename
                }
                description
                likes {
                  total
                  __typename
                }
                __typename
              }
            }
            """;

        Map<String, Object> input = new HashMap<>();
        input.put("src", FileUtils.readResourceFile(photo));
        input.put("description", description);
        input.put("country", Map.of("code", countryCode));

        return given()
                .spec(BaseApi.SPEC_GRAPHQL)
                .header("Authorization", "Bearer " + token)
                .body(Map.of(
                        "operationName", "CreatePhoto",
                        "query", query,
                        "variables", Map.of("input", input)
                ))
                .post()
                .andReturn();
    }

    @Step("Обновить фото")
    public Response updatePhoto(String token, String photoId, String base64Image, String description, String countryCode) {
        String query = """
            mutation UpdatePhoto($input: PhotoInput!) {
              photo(input: $input) {
                id
                src
                country {
                  code
                  name
                  flag
                  __typename
                }
                description
                likes {
                  total
                  __typename
                }
                __typename
              }
            }
            """;

        Map<String, Object> input = new HashMap<>();
        input.put("id", photoId);
        input.put("src", base64Image);
        input.put("description", description);
        input.put("country", Map.of("code", countryCode));

        return given()
                .spec(BaseApi.SPEC_GRAPHQL)
                .header("Authorization", token)
                .body(Map.of(
                        "operationName", "UpdatePhoto",
                        "query", query,
                        "variables", Map.of("input", input)
                ))
                .post()
                .andReturn();
    }

    @Step("Удалить фото")
    public Response deletePhoto(String token, String photoId) {
        String query = """
            mutation DeletePhoto($id: ID!) {
              deletePhoto(id: $id)
            }
            """;

        Map<String, Object> variables = Map.of(
                "id", photoId
        );

        return given()
                .spec(BaseApi.SPEC_GRAPHQL)
                .header("Authorization", "Bearer " + token)
                .body(Map.of(
                        "operationName", "DeletePhoto",
                        "query", query,
                        "variables", variables
                ))
                .post()
                .andReturn();
    }

    @Step("Поставить лайк на фото")
    public Response likePhoto(String token, String photoId, String userId) {
        String query = """
            mutation LikePhoto($input: PhotoInput!) {
              photo(input: $input) {
                id
                country {
                  code
                  name
                  flag
                  __typename
                }
                description
                likes {
                  total
                  __typename
                }
                __typename
              }
            }
            """;

        Map<String, Object> input = new HashMap<>();
        input.put("id", photoId);
        input.put("like", Map.of("user", userId));

        return given()
                .spec(BaseApi.SPEC_GRAPHQL)
                .header("Authorization", "Bearer " + token)
                .body(Map.of(
                        "operationName", "LikePhoto",
                        "query", query,
                        "variables", Map.of("input", input)
                ))
                .post()
                .andReturn();
    }
}
