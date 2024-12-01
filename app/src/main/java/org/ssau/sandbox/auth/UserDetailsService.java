package org.ssau.sandbox.auth;

import java.util.function.Function;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.ssau.sandbox.domain.user.AppUser;
import org.ssau.sandbox.repository.AppUserRepository;

import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class UserDetailsService implements ReactiveUserDetailsService {

  private final AppUserRepository rep;

  private static final  Function<AppUser, UserDetails> entityToUserDetails =
          entity -> User
          .withUsername(entity.getUsername())
          .password(entity.getPasswordHash())
          .accountExpired(false)
          .accountLocked(false)
          .roles(entity.getRole().toString())
          .build();

  @Override
  public Mono<UserDetails> findByUsername(String username) {
    return rep.findByUsername(username)
        .map(entityToUserDetails);
  }

}
