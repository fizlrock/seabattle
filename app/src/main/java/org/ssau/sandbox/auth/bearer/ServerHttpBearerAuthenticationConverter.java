
package org.ssau.sandbox.auth.bearer;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.ssau.sandbox.auth.jwt.ClaimsToAuthConverter;
import org.ssau.sandbox.auth.jwt.HeadersBearerTokenExtractor;
import org.ssau.sandbox.auth.jwt.TokenParser;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * ServerHttpBearerAuthenticationConverter
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ServerHttpBearerAuthenticationConverter implements ServerAuthenticationConverter {

  private final TokenParser claimsExtractor;
  private final ClaimsToAuthConverter claimsToAuth;

  @Override
  public Mono<Authentication> convert(ServerWebExchange serverWebExchange) {

    return Mono.just(serverWebExchange)
        .flatMap(HeadersBearerTokenExtractor::extract)
        .switchIfEmpty(Mono.empty())
        .map(BearerTokenAuthenticationToken::new);
        // .flatMap(claimsExtractor)
        // .flatMap(claimsToAuth);
  }
}
