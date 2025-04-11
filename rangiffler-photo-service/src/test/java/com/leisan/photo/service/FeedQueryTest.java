package com.leisan.photo.service;

import com.leisan.graphql.common.test.GraphqlAssertions;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.UUID;

class FeedQueryTest extends BaseTest {

    @Test
    void testGetPhotoPageable() {
        var query = """
                query MyQuery {
                    feed(withFriends: false) {
                      photos(page: 0, size: 1) {
                        edges {
                          node {
                            country {
                              code
                            }
                            creationDate
                            description
                            id
                            likes {
                              likes {
                                creationDate
                                user
                                username
                              }
                              total
                            }
                            src
                          }
                        }
                        total
                      }
                      stat {
                        count
                        country {
                          code
                        }
                      }
                      username
                      withFriends
                    }
                  }
                """;
        Map<String, Object> firstPhoto = Map.of(
                "photo", Map.of(
                        "src", "first",
                        "country", Map.of(
                                "code", "US"
                        ),
                        "description", "firstPhoto"
                )
        );
        Map<String, Object> secondPhoto = Map.of(
                "photo", Map.of(
                        "src", "second",
                        "country", Map.of(
                                "code", "RU"
                        ),
                        "description", "secondPhoto"
                )
        );
        var operationName = "createPhoto";
        var createPhotoQuery = """
                mutation createPhoto($photo: PhotoInput!) {
                  photo(input: $photo) {
                    id
                  }
                }
                """;
        graphqlClient.makeGraphqlCall(GraphqlRq.builder()
                .operationName(operationName)
                .query(createPhotoQuery)
                .variables(firstPhoto)
                .build());
        graphqlClient.makeGraphqlCall(GraphqlRq.builder()
                .operationName(operationName)
                .query(createPhotoQuery)
                .variables(secondPhoto)
                .build());

        var firstRq = GraphqlRq.builder().query(query).build();
        var secondRq = GraphqlRq.builder().query(query.replace("page: 0", "page: 1")).build();

        var firstRs = graphqlClient.makeGraphqlCall(firstRq);
        GraphqlAssertions.assertGraphqlRs(firstRs)
                .assertEqualsByJsonPath("feed.withFriends", false)
                .assertEqualsByJsonPath("feed.stat[0].count", 1)
                .assertEqualsByJsonPath("feed.photos.edges[0].node.src", "first")
                .assertEqualsByJsonPath("feed.photos.edges[0].node.country.code", "US")
                .assertEqualsByJsonPath("feed.photos.edges[0].node.description", "firstPhoto");
        var secondRs = graphqlClient.makeGraphqlCall(secondRq);
        GraphqlAssertions.assertGraphqlRs(secondRs)
                .assertEqualsByJsonPath("feed.withFriends", false)
                .assertEqualsByJsonPath("feed.stat[0].count", 1)
                .assertEqualsByJsonPath("feed.photos.edges[0].node.src", "second")
                .assertEqualsByJsonPath("feed.photos.edges[0].node.country.code", "RU")
                .assertEqualsByJsonPath("feed.photos.edges[0].node.description", "secondPhoto");
    }
}
