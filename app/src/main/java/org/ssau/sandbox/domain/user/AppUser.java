package org.ssau.sandbox.domain.user;

import java.util.Collection;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AppUser implements UserDetails {

  @Id
  private Long id;
  private String username;
  private UserRole role;
  private String passwordHash;
  private Long avatarId;

  public static enum UserRole {
    Player, Manager, Banned
  }



  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority(role.toString()));
  }

  @Override
  public String getPassword() {
    return passwordHash;
  }
}
