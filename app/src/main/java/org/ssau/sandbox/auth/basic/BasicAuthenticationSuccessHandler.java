package org.ssau.sandbox.auth.basic;

import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.ssau.sandbox.auth.jwt.JWTTokenService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * On success authentication a signed JWT object is serialized and added
 * in the authorization header as a bearer token
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class BasicAuthenticationSuccessHandler
    implements ServerAuthenticationSuccessHandler {

  private final JWTTokenService tokenService;

  /**
   * A successful authentication object us used to create a JWT object and
   * added in the authorization header of the current WebExchange
   *
   * @param webFilterExchange
   * @param authentication
   * @return
   */
  @Override
  public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {

    ServerWebExchange exchange = webFilterExchange.getExchange();


    log.info("Пользователь {} успешно аутентифицирован через HttpBasic. id: {}", authentication.getName(), webFilterExchange.hashCode());
    ResponseCookie cookie = ResponseCookie.from("Bearer", tokenFromAuthentication(authentication))
        .path(null) // Доступна для всех путей
        .httpOnly(false) // Доступ только для HTTP (недоступна из JavaScript) // TODO
        .secure(false) // Передается только через HTTPS // TODO
        // .sameSite("None") // Защита от CSRF (можно также использовать "Lax" или
        // "None") // TODO
        .maxAge(360000) // Время жизни в секундах (например, 1 час)
        .build();
    webFilterExchange.getChain().filter(exchange);
    exchange.getResponse().addCookie(cookie);

    log.info("Добавлена кука с JWT токеном");

    return webFilterExchange.getChain().filter(exchange);
  }

  // private String getHttpAuthHeaderValue(Authentication authentication) {
  // return String.join(" ", "Bearer", tokenFromAuthentication(authentication));
  // }

  private String tokenFromAuthentication(Authentication authentication) {
    return tokenService.generateToken(
        authentication.getName(),
        authentication.getAuthorities());
  }
}
