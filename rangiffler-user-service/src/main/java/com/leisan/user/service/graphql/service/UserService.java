package com.leisan.user.service.graphql.service;

import com.leisan.user.service.graphql.dto.UserConnectionGql;
import com.leisan.user.service.graphql.dto.UserGql;
import com.leisan.user.service.graphql.dto.UserInputGql;
import com.leisan.user.service.kafka.KafkaUser;

import java.util.List;

public interface UserService {

    UserGql getUser(String id);

    UserGql getUserByUsername(String username);

    UserConnectionGql getUsers(Integer page,
                               Integer size,
                               String searchQuery);

    void createUser(KafkaUser kafkaUser);

    UserGql updateUser(String username,
                       UserInputGql userInputGql);

    UserConnectionGql friends(Integer page,
                              Integer size,
                              String searchQuery,
                              String username);

    UserConnectionGql incomeInvitations(String userId,
                                        String searchQuery,
                                        Integer page,
                                        Integer size);

    UserConnectionGql outcomeInvitations(String userId,
                                         String searchQuery,
                                         Integer page,
                                         Integer size);

    List<UserGql> userFriends(String username);

    String getUserCountryCode(String username);
}
