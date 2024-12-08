package org.ssau.sandbox.auth.basic;

import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.ssau.sandbox.auth.jwt.JWTTokenService;
import org.ssau.sandbox.repository.AppUserRepository;

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
  private final AppUserRepository repa;

  @Override
  public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {

    ServerWebExchange exchange = webFilterExchange.getExchange();

    log.info("Пользователь {} успешно аутентифицирован через HttpBasic. id: {}", authentication.getName(),
        webFilterExchange.hashCode());

    return Mono.just(authentication)
        .flatMap(this::tokenFromAuthentication)
        .map(this::makeCookie)
        .map(c -> {
          exchange.getResponse().addCookie(c);
          return Mono.empty();
        })
        .flatMap(x -> webFilterExchange.getChain().filter(exchange));

    // exchange.getResponse().addCookie(cookie);
    // log.info("Добавлена кука с JWT токеном");
    // return webFilterExchange.getChain().filter(exchange);
  }

  private ResponseCookie makeCookie(String token) {
    return ResponseCookie.from("Bearer", token)
        .path(null) // Доступна для всех путей
        .httpOnly(false) // Доступ только для HTTP (недоступна из JavaScript) // TODO
        .secure(false) // Передается только через HTTPS // TODO
        // .sameSite("None") // Защита от CSRF (можно также использовать "Lax" или
        // "None") // TODO
        .maxAge(360000) // Время жизни в секундах (например, 1 час)
        .build();
  }

  private Mono<String> tokenFromAuthentication(Authentication authentication) {

    // TODO наверное стоит сделать метод AppUser -> Token
    return repa.findByUsername(authentication.getName())
        .map(user -> tokenService.generateToken(user.getUsername(), user.getId(), user.getAuthorities()));
  }
}
