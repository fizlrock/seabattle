package org.ssau.sandbox.service;

import java.util.function.Function;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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

@Slf4j
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
    dto.setAvatarId(-1l);
    dto.setPictureUrl("https://i.pinimg.com/736x/e9/77/ad/e977ad69d2c07efd37b044b85e29aff7.jpg");
    return dto;
  };

  public Mono<UserProfileDto> registerUser(Mono<RegistrationRequestBody> request) {
    return request
        .map(basicUserFactory)
        .flatMap(user -> userRepository.existsByUsername(user.getUsername())
            .flatMap(exists -> {
              if (exists) {
                return Mono.error(new IllegalArgumentException("Пользователь с таким именем уже существует"));
              } else {
                return userRepository.save(user);
              }
            }))
        .map(dtoFactory)
        .doOnError(th -> log.debug("Ошибка регистрации пользователя: {}", th))
        .doOnNext(x -> log.info("Регистрация игрока с именем: {}", x.getLogin()));
  }

  public Mono<UserProfileDto> getUserProfileByUsername(Mono<String> username) {
    return username
        .flatMap(userRepository::findByUsername)
        .map(dtoFactory);

  }

}
