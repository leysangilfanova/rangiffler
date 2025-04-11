package com.leisan.user.service.repository;

import com.leisan.user.service.model.Friendship;
import com.leisan.user.service.model.FriendshipId;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;

import java.util.Optional;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, FriendshipId> {

    Optional<Friendship> findByRequesterIdAndAddresseeId(String requesterId,
                                                         String addresseeId);
}
