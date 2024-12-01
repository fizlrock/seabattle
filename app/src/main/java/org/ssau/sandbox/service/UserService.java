package org.ssau.sandbox.service;

import java.util.function.Function;

import lombok.RequiredArgsConstructor;
import org.openapitools.model.RegistrationRequestBody;
import org.openapitools.model.UserProfileDto;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.ssau.sandbox.domain.user.AppUser;
import org.ssau.sandbox.domain.user.AppUser.UserRole;
import org.ssau.sandbox.repository.AppUserRepository;

import jakarta.annotation.PostConstruct;
import reactor.core.publisher.Mono;

/**
 * UserService
 */
@Service
@RequiredArgsConstructor
public class UserService {

  private final PasswordEncoder passEncoder;
  private final AppUserRepository userRepository;

  private Function<RegistrationRequestBody, AppUser> basicUserFactory;

  @PostConstruct
  void init() {
    basicUserFactory = (RegistrationRequestBody r) -> AppUser.builder()
        .username(r.getLogin())
        .passwordHash(passEncoder.encode(r.getPassword()))
        .role(UserRole.Player)
        .build();
  }

  private Function<AppUser, UserProfileDto> dtoFactory = user -> {
    var dto = new UserProfileDto();
    dto.setLogin(user.getUsername());
    dto.setUserId(user.getId());
    return dto;
  };

  public Mono<UserProfileDto> registerUser(Mono<RegistrationRequestBody> request) {
    return request
        .map(basicUserFactory)
        .flatMap(userRepository::save)
        .map(dtoFactory);
  }

  public Mono<UserProfileDto> getUserProfileByUsername(Mono<String> username) {
    return username
        .flatMap(userRepository::findByUsername)
        .map(dtoFactory);

  }

}
