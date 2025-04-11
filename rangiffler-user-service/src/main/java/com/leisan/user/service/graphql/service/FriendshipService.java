package com.leisan.user.service.graphql.service;

import com.leisan.user.service.graphql.dto.UserGql;
import com.leisan.user.service.model.FriendStatus;
import com.leisan.user.service.model.Friendship;
import com.leisan.user.service.model.FriendshipStatus;
import com.leisan.user.service.repository.FriendshipRepository;
import io.micronaut.transaction.annotation.Transactional;
import jakarta.annotation.Nonnull;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

@Singleton
@RequiredArgsConstructor
public class FriendshipService {

    private final UserService userService;
    private final FriendshipRepository friendshipRepository;

    public FriendStatus friendStatus(String currentUsername,
                                     String targetUsername) {
        var currentUser = getRequiredUser(currentUsername);
        var targetUser = getRequiredUser(targetUsername);

        var outcomeFriendship = getFriendshipRequest(currentUser, targetUser);
        if (outcomeFriendship.isPresent()) {
            return outcomeFriendship.map(Friendship::getStatus)
                    .map(s -> switch (s) {
                        case PENDING -> FriendStatus.INVITATION_SENT;
                        case ACCEPTED -> FriendStatus.FRIEND;
                        default -> null;
                    }).orElse(null);
        }
        var incomeFriendship = getFriendshipRequest(targetUser, currentUser);
        if (incomeFriendship.isPresent()) {
            return incomeFriendship.map(Friendship::getStatus)
                    .map(s -> switch (s) {
                        case PENDING -> FriendStatus.INVITATION_RECEIVED;
                        case ACCEPTED -> FriendStatus.FRIEND;
                        default -> null;
                    }).orElse(null);
        }
        return null;
    }

    @Transactional
    public void createFriendshipRequest(@Nonnull String username,
                                        @Nonnull String targetUsername) {
        if (Objects.equals(username, targetUsername)) {
            throw new IllegalArgumentException("Can`t create friendship request for self user");
        }
        var currentUser = getRequiredUser(username);
        var targetUser = getRequiredUser(targetUsername);
        Optional<Friendship> mayBeInvite = getFriendshipRequest(currentUser, targetUser);
        if (mayBeInvite.isPresent()) {
            mayBeInvite.get().setStatus(FriendshipStatus.ACCEPTED);
        } else {
            createFriendship(currentUser, targetUser);
        }
    }

    @Transactional
    public void acceptFriendshipRequest(@Nonnull String username,
                                        @Nonnull String targetUsername) {
        if (Objects.equals(username, targetUsername)) {
            throw new IllegalArgumentException("Can`t create friendship request for self user");
        }
        var currentUser = getRequiredUser(username);
        var targetUser = getRequiredUser(targetUsername);
        Friendship invite = getFriendshipRequest(targetUser, currentUser)
                .orElseThrow();
        invite.setStatus(FriendshipStatus.ACCEPTED);
        friendshipRepository.update(invite);
    }

    @Transactional
    public void declineFriendshipRequest(@Nonnull String username,
                                         @Nonnull String targetUsername) {
        if (Objects.equals(username, targetUsername)) {
            throw new IllegalArgumentException("Can`t create friendship request for self user");
        }
        //removeFriend(username, targetUsername);
        /*if (Objects.equals(username, targetUsername)) {
            throw new IllegalArgumentException("Can`t create friendship request for self user");
        }*/
        UserGql currentUser = getRequiredUser(username);
        UserGql targetUser = getRequiredUser(targetUsername);
        var friendship = getFriendshipRequest(currentUser, targetUser)
                .or(() -> getFriendshipRequest(targetUser, currentUser))
                .orElseThrow();
        friendship.setStatus(FriendshipStatus.DECLINED);
        friendshipRepository.update(friendship);
    }

    @Transactional
    public void removeFriend(@Nonnull String username,
                             @Nonnull String targetUsername) {
        UserGql currentUser = getRequiredUser(username);
        UserGql targetUser = getRequiredUser(targetUsername);
        var friendship = getFriendshipRequest(currentUser, targetUser)
                .or(() -> getFriendshipRequest(targetUser, currentUser))
                .orElseThrow();
        friendshipRepository.delete(friendship);
    }

    @Nonnull
    UserGql getRequiredUser(@Nonnull String username) {
        return userService.getUserByUsername(username);
    }

    @Nonnull
    private Optional<Friendship> getFriendshipRequest(@Nonnull UserGql currentUser,
                                                      @Nonnull UserGql targetUser) {
        return friendshipRepository.findByRequesterIdAndAddresseeId(currentUser.getId(), targetUser.getId());
    }

    private void createFriendship(UserGql currentUser,
                                  UserGql targetUser) {
        friendshipRepository.save(Friendship.builder()
                .requesterId(currentUser.getId())
                .addresseeId(targetUser.getId())
                .createdDate(Instant.now())
                .status(FriendshipStatus.PENDING)
                .build());
    }
}
