package org.ssau.sandbox.domain.user;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "app_user")
public class UserEntity {
  @Id
  private Long id;

  private String username;

  private UserRole role;

  private String passwordHash;

  private Avatar avatar;

}
