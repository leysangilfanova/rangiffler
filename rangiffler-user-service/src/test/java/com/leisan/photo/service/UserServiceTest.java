package com.leisan.photo.service;

import com.leisan.user.service.graphql.dto.UserConnectionGql;
import com.leisan.user.service.model.User;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class UserServiceTest extends BaseTest {

    @Test
    void findAllByQueryWithQuerySuccess() {
        for (int i = 0; i < 10; i++) {
            userRepository.save(User.builder()
                    .id("test_user_" + i)
                    .username("test_user_" + i)
                    .countryCode("ru")
                    .lastName("test")
                    .firstName("test")
                    .avatar("test")
                    .build());
        }
        Mockito.when(authService.getCurrentUsername()).thenReturn("test_user_1");

        var users = userService.getUsers(0, 10, "test_user");
        assertThat(users)
                .isNotNull()
                .extracting(UserConnectionGql::total)
                .isEqualTo(9);
        assertThat(users.edges().size()).isEqualTo(9);
    }
}
