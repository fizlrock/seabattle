
package org.ssau.sandbox.auth.jwt;

import java.util.function.Function;
import java.util.stream.Stream;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * This converter takes a SignedJWT and extracts all information
 * contained to build an Authentication Object
 * The signed JWT has already been verified.
 *
 */
@Slf4j
@Component
public class ClaimsToAuthConverter implements Function<Claims, Mono<Authentication>> {

  @Override
  public Mono<Authentication> apply(Claims claims) {
    String username = claims.getSubject();

    String[] roles = claims.get("roles", String.class)
        .split(",");

    var grantedAuthorities = Stream.of(roles)
        .map(String::strip)
        .map(SimpleGrantedAuthority::new)
        .toList();

    return Mono.just(new UsernamePasswordAuthenticationToken(username, null, grantedAuthorities));
  }

}
