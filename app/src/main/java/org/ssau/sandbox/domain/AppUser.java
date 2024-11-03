package org.ssau.sandbox.domain;

import org.springframework.data.annotation.Id;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AppUser {

  @Id
  private Long id;
  private String username;
  private UserRole role;
  private String passwordHash;

  public static enum UserRole {
    Player, Manager, Banned
  }

}
