package org.ssau.sandbox.auth;

import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.ssau.sandbox.repository.AppUserRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
@Slf4j
public class UserDetailsService implements ReactiveUserDetailsService {

  private final AppUserRepository rep;

  // private static final Function<AppUser, UserDetails> entityToUserDetails = entity -> User
  //     .withUsername(entity.getUsername())
  //     .password(entity.getPasswordHash())
  //     .accountExpired(false)
  //     .accountLocked(false)
  //     .roles(entity.getRole().toString())
  //     .build();

  @Override
  public Mono<UserDetails> findByUsername(String username) {
    log.warn("FINDING : {}", username);
    return rep.findByUsername(username).cast(UserDetails.class);
    // .map(entityToUserDetails);
  }

}
