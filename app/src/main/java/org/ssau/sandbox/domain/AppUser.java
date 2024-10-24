package org.ssau.sandbox.domain;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppUser implements UserDetails {

  private Long id;

  private String username;

  @JsonIgnore
  private String password;

  private Role role = Role.Player;

  public static enum Role {
    Player, Admin
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new GrantedAuthority() {
      @Override
      public String getAuthority() {
        return role.toString();
      }

    });
  }

}
