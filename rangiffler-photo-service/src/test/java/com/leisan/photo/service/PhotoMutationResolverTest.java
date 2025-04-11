package com.leisan.photo.service;

import com.jayway.jsonpath.JsonPath;
import com.leisan.graphql.common.test.GraphqlAssertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

class PhotoMutationResolverTest extends BaseTest {
    @Test
    void testAddPhoto() {
        var query = """
                mutation createPhoto($photo: PhotoInput!) {
                  photo(input: $photo) {
                    src
                    description
                    country {
                        code
                    }
                    likes {
                      total
                      likes {
                        user
                        creationDate
                      }
                    }
                  }
                }
                """;
        Map<String, Object> variablesMap = Map.of(
                "photo", Map.of(
                        "src", "test",
                        "country", Map.of(
                                "code", "US"
                        ),
                        "description", "my photo"
                )
        );
        var operationName = "createPhoto";
        var rq = GraphqlRq.builder()
                .operationName(operationName)
                .query(query)
                .variables(variablesMap)
                .build();

        var rs = graphqlClient.makeGraphqlCall(rq);
        GraphqlAssertions.assertGraphqlRs(rs)
                .assertEqualsByJsonPath("photo.src", "test")
                .assertEqualsByJsonPath("photo.country.code", "US")
                .assertEqualsByJsonPath("photo.description", "my photo");
    }

    @Test
    void testAddLike() {
        var createPhotoQuery = """
                mutation createPhoto($photo: PhotoInput!) {
                  photo(input: $photo) {
                    id
                    src
                    description
                    country {
                        code
                    }
                    likes {
                      total
                      likes {
                        user
                        creationDate
                      }
                    }
                  }
                }
                """;
        Map<String, Object> photoVariablesMap = Map.of(
                "photo", Map.of(
                        "src", "test",
                        "country", Map.of(
                                "code", "US"
                        ),
                        "description", "my photo"
                )
        );
        var createPhoto = "createPhoto";
        var createPhotoRq = GraphqlRq.builder()
                .operationName(createPhoto)
                .query(createPhotoQuery)
                .variables(photoVariablesMap)
                .build();

        var addLikeQuery = """
                mutation addLike($photo: PhotoInput!) {
                  photo(input: $photo) {
                    id
                    src
                    description
                    country {
                        code
                    }
                    likes {
                      total
                      likes {
                        user
                        creationDate
                      }
                    }
                  }
                }
                """;
        var likePhoto = "addLike";

        var createPhotoRs = graphqlClient.makeGraphqlCall(createPhotoRq);
        String photoId = JsonPath.read(createPhotoRs, "$.data.photo.id");

        Map<String, Object> likeVariablesMap = Map.of(
                "photo", Map.of(
                        "id", photoId,
                        "like", Map.of(
                                "user", currentUsername
                        )
                )
        );
        var likeRq = GraphqlRq.builder()
                .operationName(likePhoto)
                .query(addLikeQuery)
                .variables(likeVariablesMap)
                .build();
        var likeRs = graphqlClient.makeGraphqlCall(likeRq);
        GraphqlAssertions.assertGraphqlRs(likeRs)
                .assertEqualsByJsonPath("photo.id", photoId)
                .assertEqualsByJsonPath("photo.src", "test")
                .assertEqualsByJsonPath("photo.country.code", "US")
                .assertEqualsByJsonPath("photo.description", "my photo")
                .assertEqualsByJsonPath("photo.likes.total", 1)
                .assertEqualsByJsonPath("photo.likes.likes[0].user", currentUsername);
        ;
    }
}
