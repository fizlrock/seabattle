package org.ssau.sandbox.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.ssau.sandbox.domain.AppUser;
import org.ssau.sandbox.domain.AppUser.Role;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

/**
 * InMemoryUserRepository
 */
@Component
@RequiredArgsConstructor
public class InMemoryUserRepository implements UserRepository {

  Map<String, AppUser> userMap = new HashMap<>();
  private final PasswordEncoder encoder;

  @PostConstruct
  void init() {
    var user1 = new AppUser(0l, "fizlrock", encoder.encode("123123"), Role.Player);
    var user2 = new AppUser(0l, "njenkins", encoder.encode("222222"), Role.Admin);
    saveEntity(user1);
    saveEntity(user2);
  }

  @Override
  public Mono<AppUser> findByUsername(String username) {
    if (userMap.containsKey(username))
      return Mono.just(userMap.get(username));
    else
      return Mono.empty();
  }

  @Override
  public void saveEntity(AppUser user) {
    userMap.put(user.getUsername(), user);
  }

}
