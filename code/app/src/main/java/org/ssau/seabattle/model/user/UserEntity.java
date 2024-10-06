package org.ssau.seabattle.userModel;

import static jakarta.persistence.EnumType.STRING;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

/**
 * User
 */
@Entity
@Builder
@Table(name = "app_user")
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
  @Id
  @Column(name = "id")
  private Long id;

  @Column(name = "username")
  private String username;

  @Enumerated(STRING)
  private UserRole role;

  // @Column(name = "password_hash")
  // // TODO как прописать ограничения и стоит ли
  // private byte[] passwordHash;

}
