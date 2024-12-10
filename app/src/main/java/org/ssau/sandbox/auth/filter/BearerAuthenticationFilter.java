package org.ssau.sandbox.auth.filter;

import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.web.server.ServerWebExchange;
import org.ssau.sandbox.auth.jwt.HeadersBearerTokenExtractor;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
public class BearerAuthenticationFilter extends AuthenticationWebFilter {

  // TODO ужос
  private static ReactiveAuthenticationManager authenticationManager = x -> Mono.just(x);

  ServerAuthenticationConverter converter = (ServerWebExchange exch) -> {
    log.info("Попытка вытащить токен из запроса");

    return Mono.just(exch)
        .flatMap(HeadersBearerTokenExtractor::extract)
        .map(BearerTokenAuthenticationToken::new)
        .log()
        .cast(Authentication.class);
  };

  public BearerAuthenticationFilter() {
    super(authenticationManager);
    this.setServerAuthenticationConverter(converter);
    // this.setRequiresAuthenticationMatcher(ServerWebExchangeMatchers.pathMatchers("/user/**"));
  }

}
