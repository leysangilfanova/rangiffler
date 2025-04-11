package com.leisan.user.service.model;


import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@IdClass(FriendshipId.class)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Friendship {
    @Id
    private String requesterId;

    @Id
    private String addresseeId;

    private Instant createdDate;

    @Enumerated(EnumType.STRING)
    private FriendshipStatus status;
}
