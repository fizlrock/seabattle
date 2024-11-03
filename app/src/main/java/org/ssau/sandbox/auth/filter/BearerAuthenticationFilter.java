package org.ssau.sandbox.auth.filter;

import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import org.springframework.stereotype.Component;
import org.ssau.sandbox.auth.bearer.ServerHttpBearerAuthenticationConverter;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class BearerAuthenticationFilter extends AuthenticationWebFilter {

  private static ReactiveAuthenticationManager authenticationManager = x -> Mono.just(x);

  public BearerAuthenticationFilter(
      ServerHttpBearerAuthenticationConverter converter) {
    super(authenticationManager);
    this.setServerAuthenticationConverter(converter);
    this.setRequiresAuthenticationMatcher(ServerWebExchangeMatchers.pathMatchers("/user/**"));
  }

}
