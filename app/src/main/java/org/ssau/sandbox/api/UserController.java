package org.ssau.sandbox.api;

import org.openapitools.api.AvatarApi;
import org.openapitools.api.UserApi;
import org.openapitools.model.AvatarDto;
import org.openapitools.model.RegistrationRequestBody;
import org.openapitools.model.UserProfileDto;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.DefaultOAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
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
  public Mono<Void> setAvatar(
      @Min(0) Long userId,
      @Min(0) Long avatarId,
      ServerWebExchange exchange) {
    throw new UnsupportedOperationException("Unimplemented method 'setAvatar'");
  }

  @Override
  public Flux<AvatarDto> getAvatars(ServerWebExchange exchange) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getAvatars'");
  }

}
