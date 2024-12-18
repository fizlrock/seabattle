package org.ssau.sandbox.auth.filter;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.web.server.ServerWebExchange;
import org.ssau.sandbox.auth.bearer.BearerTokenAuthenticationManager;
import org.ssau.sandbox.auth.jwt.HeadersBearerTokenExtractor;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
public class BearerAuthenticationFilter extends AuthenticationWebFilter {

  /**
   * Анонимный класс должен вытащить токен из запроса.
   * Если токена нет - вернуть Mono.empty()
   */
  private ServerAuthenticationConverter converter = (ServerWebExchange exch) -> {
    return Mono.just(exch)
        .flatMap(HeadersBearerTokenExtractor::extract)
        .switchIfEmpty(Mono.empty())
        .map(BearerTokenAuthenticationToken::new)
        .cast(Authentication.class);
  };

  public BearerAuthenticationFilter(BearerTokenAuthenticationManager authenticationManager) {
    super(authenticationManager);
    this.setServerAuthenticationConverter(converter);
    // this.setRequiresAuthenticationMatcher(ServerWebExchangeMatchers.pathMatchers("/user/**"));
  }

}
