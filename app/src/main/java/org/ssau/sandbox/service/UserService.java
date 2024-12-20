package org.ssau.sandbox.service;

import java.util.function.Function;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.openapitools.model.RegistrationRequestBody;
import org.openapitools.model.UserProfileDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.ssau.sandbox.domain.user.AppUser;
import org.ssau.sandbox.domain.user.AppUser.UserRole;
import org.ssau.sandbox.repository.AppUserRepository;
import org.ssau.sandbox.repository.AvatarRepository;

import jakarta.annotation.PostConstruct;
import reactor.core.publisher.Mono;

/**
 * UserService
 */
@Service

@Slf4j
@RequiredArgsConstructor
public class UserService {

  @Autowired
  private PasswordEncoder passEncoder;
  @Autowired
  private AppUserRepository userRepository;
  @Autowired
  private AvatarRepository avatarRepository;

  private Function<RegistrationRequestBody, AppUser> basicUserFactory;

  @PostConstruct
  void init() {
    basicUserFactory = (RegistrationRequestBody r) -> AppUser.builder()
        .username(r.getLogin())
        .passwordHash(passEncoder.encode(r.getPassword()))
        .role(UserRole.Player)
        .avatarId(0l) // default avatar
        .build();
  }

  private Function<AppUser, Mono<UserProfileDto>> dtoFactory = user -> {
    var dto = new UserProfileDto();
    dto.setLogin(user.getUsername());
    dto.setUserId(user.getId());
    dto.setAvatarId(user.getAvatarId());

    return avatarRepository.findById(user.getAvatarId()).map(avatar -> {
      dto.setPictureUrl(avatar.getPicture_url());
      return dto;
    });
    // TODO
  };

  public Mono<UserProfileDto> registerUser(Mono<RegistrationRequestBody> request) {
    return request
        .map(basicUserFactory)
        .flatMap(user -> userRepository.existsByUsername(user.getUsername())
            .flatMap(exists -> {
              if (exists)
                return Mono.error(new IllegalArgumentException("Пользователь с таким именем уже существует"));
              else
                return userRepository.save(user);

            }))
        .flatMap(dtoFactory)
        .doOnError(th -> log.debug("Ошибка регистрации пользователя: {}", th))
        .doOnNext(x -> log.info("Регистрация игрока с именем: {}", x.getLogin()));
  }

  public Mono<UserProfileDto> getUserProfileByUsername(Mono<String> username) {
    return username
        .flatMap(userRepository::findByUsername)
        .flatMap(dtoFactory);

  }

}
