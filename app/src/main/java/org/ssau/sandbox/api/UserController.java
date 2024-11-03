package org.ssau.sandbox.api;

import org.openapitools.api.UserApi;
import org.openapitools.model.RegistrationRequestBody;
import org.openapitools.model.UserProfileDto;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import org.ssau.sandbox.service.UserService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

/**
 * UserController
 */
@RestController
@RequiredArgsConstructor
public class UserController implements UserApi {

  private final UserService userService;

  @Override
  public Mono<UserProfileDto> getUserProfile(
      ServerWebExchange exchange) {
    return exchange.getPrincipal()
        .cast(UsernamePasswordAuthenticationToken.class)
        .map(UsernamePasswordAuthenticationToken::getPrincipal)
        .cast(String.class)
        .map(Mono::just)
        .flatMap(userService::getUserProfileByUsername);
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

}
