package com.leisan.user.service.graphql.dto;

import com.leisan.user.service.model.FriendStatus;
import io.micronaut.serde.annotation.Serdeable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Serdeable
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserGql {
    private String id;
    private String username;
    private String firstname;
    private String surname;
    private String avatar;
    private FriendStatus friendStatus;
    private UserConnectionGql friends;
    private UserConnectionGql incomeInvitations;
    private UserConnectionGql outcomeInvitations;
    private CountryDto location;
}
