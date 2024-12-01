
package org.ssau.sandbox.auth.bearer;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.ssau.sandbox.auth.jwt.AuthorizationHeaderPayload;
import org.ssau.sandbox.auth.jwt.TokenClaimsExtractor;
import org.ssau.sandbox.auth.jwt.ClaimsToAuthConverter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * This converter extracts a bearer token from a WebExchange and
 * returns an Authentication object if the JWT token is valid.
 * Validity means is well formed and signature is correct
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ServerHttpBearerAuthenticationConverter implements ServerAuthenticationConverter {

  private final TokenClaimsExtractor claimsExtractor;
  private final ClaimsToAuthConverter claimsToAuth;

  /**
   * Apply this function to the current WebExchange, an Authentication object
   * is returned when completed.
   *
   * @param serverWebExchange
   * @return
   */
  @Override
  public Mono<Authentication> convert(ServerWebExchange serverWebExchange) {
    log.info("Попытка аутентифицироваться через token");
    return Mono.empty();
    // return Mono.just(serverWebExchange)
    // .flatMap(AuthorizationHeaderPayload::extract)
    // .switchIfEmpty(Mono.empty())
    // .flatMap(claimsExtractor)
    // .flatMap(claimsToAuth);
  }
}
