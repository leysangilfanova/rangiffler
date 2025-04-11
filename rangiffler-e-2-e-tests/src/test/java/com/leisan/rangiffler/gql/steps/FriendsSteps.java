package com.leisan.rangiffler.gql.steps;

import com.leisan.rangiffler.gql.FriendsQueryType;
import com.leisan.rangiffler.gql.FriendshipActionType;
import com.leisan.rangiffler.gql.dto.FriendsResponse;
import com.leisan.rangiffler.gql.dto.FriendshipResponse;
import com.leisan.rangiffler.gql.dto.IncomeInvitationsResponse;
import com.leisan.rangiffler.gql.dto.OutcomeInvitationsResponse;
import com.leisan.rangiffler.gql.dto.SearchResponse;
import com.leisan.rangiffler.model.testdata.TestUser;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.util.List;
import java.util.stream.Collectors;

import static com.leisan.rangiffler.gql.api.BaseApi.FRIENDS_API;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;

public class FriendsSteps {

    @Step("Проверить ответ на запрос списка друзей")
    public void assertFriendsResponse(Response response,
                                      List<TestUser> expectedUsers,
                                      boolean expectedHasPreviousPage,
                                      boolean expectedHasNextPage,
                                      List<String> expectedUsernames,
                                      String expectedCountryCode,
                                      String expectedCountryName,
                                      List<String> expectedFriendStatuses) {
        // Проверка статус кода
        assertThat(response.statusCode()).isEqualTo(200);

        // Десериализация ответа
        FriendsResponse friendsResponse = response.as(FriendsResponse.class);

        // Логирование содержимого ответа для отладки
        System.out.println("Response body: " + response.body().asString());

        // Проверка пагинации
        FriendsResponse.PageInfo pageInfo = friendsResponse.getData().getUser().getFriends().getPageInfo();
        if (pageInfo != null) {
            assertThat(pageInfo.getHasPreviousPage()).isEqualTo(expectedHasPreviousPage);
            assertThat(pageInfo.getHasNextPage()).isEqualTo(expectedHasNextPage);
        } else {
            // Если PageInfo равен null, выбрасываем исключение
            throw new AssertionError("PageInfo is null in the response");
        }

        // Получаем список фактических пользователей из ответа
        List<FriendsResponse.Node> actualNodes = friendsResponse.getData().getUser().getFriends().getEdges()
                .stream()
                .map(FriendsResponse.Edge::getNode)
                .collect(Collectors.toList());

        // Проверка количества пользователей
        assertThat(actualNodes).hasSize(expectedUsers.size());

        // Проверка каждого пользователя
        for (int i = 0; i < expectedUsers.size(); i++) {
            FriendsResponse.Node actualNode = actualNodes.get(i);
            String expectedStatus = expectedFriendStatuses.get(i);

            assertThat(actualNode.getId()).isNotNull();
            assertThat(actualNode.getUsername()).isIn(expectedUsernames);
            assertThat(actualNode.getFirstname()).isEqualTo(null);
            assertThat(actualNode.getSurname()).isEqualTo(null);
            if (actualNode.getAvatar() != null) {
                assertThat(actualNode.getAvatar()).startsWith("data:image/");
            }

            if (actualNode.getLocation() != null) {
                assertThat(actualNode.getLocation().getCode()).isEqualTo(expectedCountryCode);
                assertThat(actualNode.getLocation().getName()).isEqualTo(expectedCountryName);
                if (actualNode.getLocation().getFlag() != null) {
                    assertThat(actualNode.getLocation().getFlag()).startsWith("data:image/");
                }
            }

            // Проверка статуса дружбы
            if (expectedStatus == null) {
                assertThat(actualNode.getFriendStatus()).isNull();
            } else {
                assertThat(actualNode.getFriendStatus()).isEqualTo(expectedStatus);
            }
        }

        // Проверка на наличие ошибок в ответе
        assertNull(response.path("errors"), "Ответ содержит ошибку (errors): " + response.body().asString());
    }

    @Step("Проверить ответ на запрос списка людей")
    public void assertPeopleResponse(Response response,
                                     List<TestUser> expectedUsers,
                                     boolean expectedHasPreviousPage,
                                     boolean expectedHasNextPage,
                                     List<String> expectedUsernames,
                                     String expectedCountryCode,
                                     String expectedCountryName,
                                     List<String> expectedFriendStatuses) {
        // Проверка статус кода
        assertThat(response.statusCode()).isEqualTo(200);

        // Десериализация ответа
        SearchResponse searchResponse = response.as(SearchResponse.class);

        // Проверка пагинации
        assertThat(searchResponse.getData().getUsers().getPageInfo().isHasPreviousPage())
                .isEqualTo(expectedHasPreviousPage);
        assertThat(searchResponse.getData().getUsers().getPageInfo().isHasNextPage())
                .isEqualTo(expectedHasNextPage);

        // Получаем список фактических пользователей из ответа
        List<SearchResponse.Node> actualNodes = searchResponse.getData().getUsers().getEdges()
                .stream()
                .map(SearchResponse.Edge::getNode)
                .collect(Collectors.toList());

        // Проверка количества пользователей
        assertThat(actualNodes).hasSize(expectedUsers.size());

        // Проверка каждого пользователя
        for (int i = 0; i < expectedUsers.size(); i++) {
            SearchResponse.Node actualNode = actualNodes.get(i);
            String expectedStatus = expectedFriendStatuses.get(i);

            assertThat(actualNode.getId()).isNotNull();
            assertThat(actualNode.getUsername()).isIn(expectedUsernames);
            assertThat(actualNode.getFirstname()).isEqualTo(null);
            assertThat(actualNode.getSurname()).isEqualTo(null);
            if (actualNode.getAvatar() != null) {
                assertThat(actualNode.getAvatar()).startsWith("data:image/");
            }

            if (actualNode.getLocation() != null) {
                assertThat(actualNode.getLocation().getCode()).isEqualTo(expectedCountryCode);
                assertThat(actualNode.getLocation().getName()).isEqualTo(expectedCountryName);
                if (actualNode.getLocation().getFlag() != null) {
                    assertThat(actualNode.getLocation().getFlag()).startsWith("data:image/");
                }
            }

            if (expectedStatus == null) {
                assertThat(actualNode.getFriendStatus()).isNull();
            } else {
                assertThat(actualNode.getFriendStatus()).isEqualTo(expectedStatus);
            }
        }
        assertNull(response.path("errors"), "Ответ содержит ошибку (errors): " + response.body().asString());
    }

    @Step("Проверить ответ на запрос входящих заявок в друзья")
    public void assertIncomeInvitationsResponse(Response response,
                                      List<TestUser> expectedUsers,
                                      boolean expectedHasPreviousPage,
                                      boolean expectedHasNextPage,
                                      List<String> expectedUsernames,
                                      String expectedCountryCode,
                                      String expectedCountryName,
                                      List<String> expectedFriendStatuses) {
        // Проверка статус кода
        assertThat(response.statusCode()).isEqualTo(200);

        // Десериализация ответа
        IncomeInvitationsResponse incomeInvitationsResponse = response.as(IncomeInvitationsResponse.class);

        // Логирование содержимого ответа для отладки
        System.out.println("Response body: " + response.body().asString());

        // Проверка пагинации
        IncomeInvitationsResponse.PageInfo pageInfo = incomeInvitationsResponse.getData().getUser().getIncomeInvitations().getPageInfo();
        if (pageInfo != null) {
            assertThat(pageInfo.getHasPreviousPage()).isEqualTo(expectedHasPreviousPage);
            assertThat(pageInfo.getHasNextPage()).isEqualTo(expectedHasNextPage);
        } else {
            // Если PageInfo равен null, выбрасываем исключение
            throw new AssertionError("PageInfo is null in the response");
        }

        // Получаем список фактических пользователей из ответа
        List<IncomeInvitationsResponse.Node> actualNodes = incomeInvitationsResponse.getData().getUser().getIncomeInvitations().getEdges()
                .stream()
                .map(IncomeInvitationsResponse.Edge::getNode)
                .collect(Collectors.toList());

        // Проверка количества пользователей
        assertThat(actualNodes).hasSize(expectedUsers.size());

        // Проверка каждого пользователя
        for (int i = 0; i < expectedUsers.size(); i++) {
            IncomeInvitationsResponse.Node actualNode = actualNodes.get(i);
            String expectedStatus = expectedFriendStatuses.get(i);

            assertThat(actualNode.getId()).isNotNull();
            assertThat(actualNode.getUsername()).isIn(expectedUsernames);
            assertThat(actualNode.getFirstname()).isEqualTo(null);
            assertThat(actualNode.getSurname()).isEqualTo(null);
            if (actualNode.getAvatar() != null) {
                assertThat(actualNode.getAvatar()).startsWith("data:image/");
            }

            if (actualNode.getLocation() != null) {
                assertThat(actualNode.getLocation().getCode()).isEqualTo(expectedCountryCode);
                assertThat(actualNode.getLocation().getName()).isEqualTo(expectedCountryName);
                if (actualNode.getLocation().getFlag() != null) {
                    assertThat(actualNode.getLocation().getFlag()).startsWith("data:image/");
                }
            }

            // Проверка статуса дружбы
            if (expectedStatus == null) {
                assertThat(actualNode.getFriendStatus()).isNull();
            } else {
                assertThat(actualNode.getFriendStatus()).isEqualTo(expectedStatus);
            }
        }

        // Проверка на наличие ошибок в ответе
        assertNull(response.path("errors"), "Ответ содержит ошибку (errors): " + response.body().asString());
    }


    @Step("Проверить ответ на запрос исходящих заявок в друзья")
    public void assertOutcomeInvitationsResponse(Response response,
                                                List<TestUser> expectedUsers,
                                                boolean expectedHasPreviousPage,
                                                boolean expectedHasNextPage,
                                                List<String> expectedUsernames,
                                                String expectedCountryCode,
                                                String expectedCountryName,
                                                List<String> expectedFriendStatuses) {
        // Проверка статус кода
        assertThat(response.statusCode()).isEqualTo(200);

        // Десериализация ответа
        OutcomeInvitationsResponse outcomeInvitationsResponse = response.as(OutcomeInvitationsResponse.class);

        // Логирование содержимого ответа для отладки
        System.out.println("Response body: " + response.body().asString());

        // Проверка пагинации
        OutcomeInvitationsResponse.PageInfo pageInfo = outcomeInvitationsResponse.getData().getUser().getOutcomeInvitations().getPageInfo();
        if (pageInfo != null) {
            assertThat(pageInfo.getHasPreviousPage()).isEqualTo(expectedHasPreviousPage);
            assertThat(pageInfo.getHasNextPage()).isEqualTo(expectedHasNextPage);
        } else {
            // Если PageInfo равен null, выбрасываем исключение
            throw new AssertionError("PageInfo is null in the response");
        }

        // Получаем список фактических пользователей из ответа
        List<OutcomeInvitationsResponse.Node> actualNodes = outcomeInvitationsResponse.getData().getUser().getOutcomeInvitations().getEdges()
                .stream()
                .map(OutcomeInvitationsResponse.Edge::getNode)
                .collect(Collectors.toList());

        // Проверка количества пользователей
        assertThat(actualNodes).hasSize(expectedUsers.size());

        // Проверка каждого пользователя
        for (int i = 0; i < expectedUsers.size(); i++) {
            OutcomeInvitationsResponse.Node actualNode = actualNodes.get(i);
            String expectedStatus = expectedFriendStatuses.get(i);

            assertThat(actualNode.getId()).isNotNull();
            assertThat(actualNode.getUsername()).isIn(expectedUsernames);
            assertThat(actualNode.getFirstname()).isEqualTo(null);
            assertThat(actualNode.getSurname()).isEqualTo(null);
            if (actualNode.getAvatar() != null) {
                assertThat(actualNode.getAvatar()).startsWith("data:image/");
            }

            if (actualNode.getLocation() != null) {
                assertThat(actualNode.getLocation().getCode()).isEqualTo(expectedCountryCode);
                assertThat(actualNode.getLocation().getName()).isEqualTo(expectedCountryName);
                if (actualNode.getLocation().getFlag() != null) {
                    assertThat(actualNode.getLocation().getFlag()).startsWith("data:image/");
                }
            }

            // Проверка статуса дружбы
            if (expectedStatus == null) {
                assertThat(actualNode.getFriendStatus()).isNull();
            } else {
                assertThat(actualNode.getFriendStatus()).isEqualTo(expectedStatus);
            }
        }

        // Проверка на наличие ошибок в ответе
        assertNull(response.path("errors"), "Ответ содержит ошибку (errors): " + response.body().asString());
    }

    @Step("Получить ID пользователя #{userIndex} и выполнить действие {action}")
    public Response executeActionWithUser(String token,
                                          FriendshipActionType action,
                                          int userIndex) {
        Response peopleResponse = FRIENDS_API.executeFriendsQuery(
                FriendsQueryType.PEOPLE,
                token,
                0,
                10,
                ""
        );

        SearchResponse friendsResponse = peopleResponse.as(SearchResponse.class);
        String userId = friendsResponse.getData().getUsers().getEdges()
                .get(userIndex)
                .getNode()
                .getId();

        return FRIENDS_API.executeFriendshipAction(token, userId, action);
    }

    @Step("Выполнить действие {action} с пользователем")
    public Response executeAction(String token,
                                          FriendshipActionType action,
                                          String username) {
        return FRIENDS_API.executeFriendshipAction(token, username, action);
    }

    @Step("Проверить ответ на действие с дружбой")
    public void assertFriendshipResponse(Response response,
                                                String expectedUsername,
                                                String expectedFriendStatus) {
        // Проверка статус кода
        assertThat(response.statusCode()).isEqualTo(200);

        // Десериализация ответа
        FriendshipResponse friendshipResponse = response.as(FriendshipResponse.class);
        FriendshipResponse.Friendship friendship = friendshipResponse.getData().getFriendship();

        // Проверки полей ответа
        assertThat(friendship.getId()).isNotNull();
        assertThat(friendship.getUsername()).isEqualTo(expectedUsername);

        if (expectedFriendStatus == null) {
            assertThat(friendship.getFriendStatus()).isNull();
        } else {
            assertThat(friendship.getFriendStatus()).isEqualTo(expectedFriendStatus);
        }
        assertNull(response.path("errors"), "Ответ содержит ошибку (errors): " + response.body().asString());
    }
}
