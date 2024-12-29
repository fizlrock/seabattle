package org.ssau.sandbox.api;

import org.openapitools.api.AvatarApi;
import org.openapitools.api.UserApi;
import org.openapitools.model.AvatarDto;
import org.openapitools.model.GameStatsDto;
import org.openapitools.model.RegistrationRequestBody;
import org.openapitools.model.UserProfileDto;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.DefaultOAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import org.ssau.sandbox.domain.game.DtoFactory;
import org.ssau.sandbox.repository.AppUserRepository;
import org.ssau.sandbox.repository.AvatarRepository;
import org.ssau.sandbox.repository.GameStatsRepository;
import org.ssau.sandbox.service.UserService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * UserController
 */
@RestController
@RequiredArgsConstructor
public class UserController implements UserApi, AvatarApi {

  private final UserService userService;
  private final AvatarRepository avaRep;
  private final AppUserRepository userRep;
  private final DtoFactory dtoFactory;

  private final GameStatsRepository statsRepa;

  private Mono<DefaultOAuth2AuthenticatedPrincipal> getPrincipal(ServerWebExchange exchange) {

    return exchange.getPrincipal()
        .cast(BearerTokenAuthentication.class)
        .map(BearerTokenAuthentication::getPrincipal)
        .cast(DefaultOAuth2AuthenticatedPrincipal.class);
  }

  @Override
  public Mono<UserProfileDto> getUserProfile(
      ServerWebExchange exchange) {

    return getPrincipal(exchange)
        .map(DefaultOAuth2AuthenticatedPrincipal::getName)
        .flatMap(x -> userService.getUserProfileByUsername(Mono.just(x)))
        .switchIfEmpty(Mono.error(new UsernameNotFoundException("Пользователь не  найден")));

  }

  @Override
  public Mono<UserProfileDto> registerUser(
      @Valid Mono<RegistrationRequestBody> regRequest,
      ServerWebExchange exchange) {
    return userService.registerUser(regRequest);
  }

  @Override
  public Flux<AvatarDto> getAvatars(ServerWebExchange exchange) {

    return avaRep.findAll()
        .map(
            x -> {
              var dto = new AvatarDto();
              dto.avatarId(x.getId());
              dto.pictureUrl(x.getPicture_url());
              return dto;
            });
  }

  @Override
  public Flux<GameStatsDto> getStats(@Min(0) @Valid Long count, ServerWebExchange exchange) {
    var principal = getPrincipal(exchange);
    var id = principal.map(
        x -> x.getAttribute("user_id"))
        .cast(Integer.class)
        .map(x -> x.longValue());

    return id.flatMapMany(x -> statsRepa.getPersonalStat(x, count));
  }

  @Override
  public Mono<Void> setAvatar(@Min(0) Long avatarId, ServerWebExchange exchange) {

    var principal = getPrincipal(exchange);
    return principal.map(
        x -> x.getAttribute("user_id"))
        .cast(Integer.class)
        .map(x -> x.longValue())
        .flatMap(id -> userRep.findById(id))
        .doOnNext(u -> u.setAvatarId(avatarId))
        .flatMap(u -> userRep.save(u))
        .then();

  }

  @Override
  public Mono<UserProfileDto> getUserProfileById(@Min(0) Long userId, ServerWebExchange exchange) {
    return userRep
        .findById(userId)
        .flatMap(u -> dtoFactory.toDto(u))
        .switchIfEmpty(Mono.error(new UsernameNotFoundException("Пользователь с указанным ID не найден")));
  }

}
