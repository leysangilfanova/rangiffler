package com.leisan.rangiffler.model.testdata;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.UUID;

@Builder
@Getter
@Setter
@ToString(exclude = "avatar")
public class TestUser {

  private UUID id;
  private String username;
  private String firstname;
  private String lastName;
  private byte[] avatar;
  private List<TestUser> friends;
  private List<TestUser> incomeInvitations;
  private List<TestUser> outcomeInvitations;
  @JsonIgnore
  private TestData testData;
}
