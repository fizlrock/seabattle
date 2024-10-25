package org.ssau.sandbox.auth.filter;

import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import org.springframework.stereotype.Component;
// import org.ssau.sandbox.auth.bearer.BearerTokenReactiveAuthenticationManager;
import org.ssau.sandbox.auth.bearer.ServerHttpBearerAuthenticationConverter;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * BearerAuthenticationFilter
 */
@Component
@Slf4j
public class BearerAuthenticationFilter extends AuthenticationWebFilter {

  private static ReactiveAuthenticationManager authenticationManager = new ReactiveAuthenticationManager() {

    @Override
    public Mono<Authentication> authenticate(Authentication auth) {
      log.info("BearerReactiveAuthenticationManager:{}", auth);
      return Mono.just(auth);
    }

    @Override
    public String toString() {
      return "BearerAuthAuthenticationManager";
    }
  };

  public BearerAuthenticationFilter(
      ServerHttpBearerAuthenticationConverter converter) {
    super(authenticationManager);
    this.setAuthenticationConverter(converter); // TODO
    this.setRequiresAuthenticationMatcher(ServerWebExchangeMatchers.pathMatchers("/api/**"));
    // TODO тут этого не должно быть
  }

}
