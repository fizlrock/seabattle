package org.ssau.sandbox.auth.jwt;

import java.util.function.Function;
import java.util.stream.Stream;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.DefaultOAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class ClaimsToAuthConverter implements Function<Claims, Mono<Authentication>> {

  @Override
  public Mono<Authentication> apply(Claims claims) {

    String username = claims.getSubject();

    String[] roles = claims.get("roles", String.class)
        .split(",");

    var authorities = Stream.of(roles)
        .map(String::strip)
        .map(SimpleGrantedAuthority::new)
        .map(x -> (GrantedAuthority) x)
        .toList();

    var principal = new DefaultOAuth2AuthenticatedPrincipal(
        claims,
        authorities);

    var token = "fucking token"; // TODO

    var auth = new BearerTokenAuthentication(principal, null, null);

    return Mono.just(auth);
  }

}
