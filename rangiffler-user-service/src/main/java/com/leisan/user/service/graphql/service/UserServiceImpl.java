package com.leisan.user.service.graphql.service;

import com.leisan.user.service.graphql.dto.PageInfoGql;
import com.leisan.user.service.graphql.dto.UserConnectionGql;
import com.leisan.user.service.graphql.dto.UserEdgeGql;
import com.leisan.user.service.graphql.dto.UserGql;
import com.leisan.user.service.graphql.dto.UserInputGql;
import com.leisan.user.service.kafka.KafkaUser;
import com.leisan.user.service.mapper.UserGqlMapper;
import com.leisan.user.service.model.User;
import com.leisan.user.service.repository.UserRepository;
import io.micronaut.transaction.annotation.Transactional;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Singleton
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final CountryService countryService;
    private final AuthService authService;

    @Override
    public UserGql getUser(String id) {
        return userRepository.findById(id).map(UserGqlMapper.INSTANCE::toGql).orElseThrow();
    }

    @Override
    public UserGql getUserByUsername(String username) {
        return userRepository.find(username).map(UserGqlMapper.INSTANCE::toGql)
                .orElseThrow();
    }

    @Override
    public UserConnectionGql getUsers(Integer page,
                                      Integer size,
                                      String searchQuery) {
        var username = authService.getCurrentUsername();
        var query = StringUtils.isEmpty(searchQuery) ? null : "%" + searchQuery + "%";
        var users = userRepository.findAllByQuery(query, username, page, size);
        var total = userRepository.countAllByQuery(query, username);
        return new UserConnectionGql(
                total,
                users.stream()
                        .map(UserGqlMapper.INSTANCE::toGql)
                        .map(UserEdgeGql::new)
                        .toList(),
                new PageInfoGql(page > 0, users.size() == size)
        );
    }

    @Transactional
    @Override
    public void createUser(KafkaUser kafkaUser) {
        var user = userRepository.find(kafkaUser.username());
        if (user.isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }
        userRepository.save(
                User.builder()
                        .id(kafkaUser.username())
                        .username(kafkaUser.username())
                        .firstName(null)
                        .lastName(null)
                        .countryCode("ru")
                        .avatar("data:image/jpeg;base64,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")
                        .build());
    }

    @SneakyThrows
    @Override
    public UserGql updateUser(String username,
                              UserInputGql userInputGql) {
        var currentUser = userRepository.find(username).orElse(null);
        if (currentUser == null) {
            throw new IllegalArgumentException("User not found");
        }
        if (userInputGql.location() != null) {
            var countryExists = countryService.countryExists(userInputGql.location().code()).toFuture().get();
            if (!countryExists) {
                throw new IllegalArgumentException("Country not found");
            }
        }
        currentUser.setFirstName(userInputGql.firstname());
        currentUser.setLastName(userInputGql.surname());
        currentUser.setCountryCode(userInputGql.location().code());
        currentUser.setAvatar(userInputGql.avatar());
        userRepository.update(currentUser);
        return UserGqlMapper.INSTANCE.toGql(currentUser);
    }

    @Override
    public UserConnectionGql friends(Integer page,
                                     Integer size,
                                     String searchQuery,
                                     String username) {
        var friendsIncome = userRepository.findAllFriendsIncome(username);
        var friendsOutcome = userRepository.findAllFriendsOutcome(username);
        var friends = new HashSet<>(friendsIncome);
        friends.addAll(friendsOutcome);
        var total = friends.size();
        var pageFriends = friends.stream()
                .filter(filterUsers(searchQuery))
                .skip((long) page * size)
                .limit(size)
                .map(UserGqlMapper.INSTANCE::toGql)
                .map(UserEdgeGql::new)
                .toList();
        return new UserConnectionGql(total,
                pageFriends,
                new PageInfoGql(page > 0, pageFriends.size() == size));
    }

    private Predicate<User> filterUsers(String searchQuery) {
        if (StringUtils.isEmpty(searchQuery)) {
            return u -> true;
        }
        return (user) -> Stream.of(user.getUsername(), user.getLastName(), user.getFirstName())
                .filter(Objects::nonNull)
                .anyMatch(v -> v.contains(searchQuery));
    }

    @Override
    public UserConnectionGql incomeInvitations(String userId,
                                               String searchQuery,
                                               Integer page,
                                               Integer size) {
        var query = StringUtils.isEmpty(searchQuery) ? null : "%" + searchQuery + "%";
        var users = userRepository.findAllIncome(userId, query, page, size);
        var total = userRepository.countAllIncome(userId, query);
        var pageUsers = users.stream()
                .map(UserGqlMapper.INSTANCE::toGql)
                .map(UserEdgeGql::new)
                .toList();
        return new UserConnectionGql(total,
                pageUsers,
                new PageInfoGql(page > 0, pageUsers.size() == size));
    }

    @Override
    public UserConnectionGql outcomeInvitations(String userId,
                                                String searchQuery,
                                                Integer page,
                                                Integer size) {
        var query = StringUtils.isEmpty(searchQuery) ? null : "%" + searchQuery + "%";
        var users = userRepository.findAllOutcome(userId, query, page, size);
        var total = userRepository.countAllOutcome(userId, query);
        var pageUsers = users.stream()
                .map(UserGqlMapper.INSTANCE::toGql)
                .map(UserEdgeGql::new)
                .toList();
        return new UserConnectionGql(total,
                pageUsers,
                new PageInfoGql(page > 0, pageUsers.size() == size)
        );
    }

    @Override
    public List<UserGql> userFriends(String username) {
        int page = 0;
        UserConnectionGql friends;
        List<UserGql> userFriends = new ArrayList<>();
        do {
            friends = friends(page, 1000, null, username);
            userFriends.addAll(friends.edges().stream().map(UserEdgeGql::node).toList());
            page++;
        } while (!friends.edges().isEmpty());
        return userFriends;
    }

    @Override
    public String getUserCountryCode(String username) {
        return userRepository.find(username).map(User::getCountryCode).orElse(null);
    }
}
