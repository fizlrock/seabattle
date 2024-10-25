package org.ssau.sandbox.auth.filter;

import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import org.springframework.stereotype.Component;
// import org.ssau.sandbox.auth.bearer.BearerTokenReactiveAuthenticationManager;
import org.ssau.sandbox.auth.bearer.ServerHttpBearerAuthenticationConverter;

import reactor.core.publisher.Mono;

/**
 * BearerAuthenticationFilter
 */
@Component
public class BearerAuthenticationFilter extends AuthenticationWebFilter {

  private static ReactiveAuthenticationManager authenticationManager = x -> Mono.just(x);

  public BearerAuthenticationFilter(
      // BearerTokenReactiveAuthenticationManager authenticationManager,
      ServerHttpBearerAuthenticationConverter converter) {
    super(authenticationManager);
    this.setAuthenticationConverter(converter); // TODO
    this.setRequiresAuthenticationMatcher(ServerWebExchangeMatchers.pathMatchers("/api/**"));
    // TODO тут этого не должно быть
  }

}
