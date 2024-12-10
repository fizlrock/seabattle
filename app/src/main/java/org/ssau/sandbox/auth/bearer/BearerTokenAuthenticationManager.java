package org.ssau.sandbox.auth.bearer;

import java.time.Instant;
import java.util.List;

import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.DefaultOAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken.TokenType;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.ssau.sandbox.auth.jwt.JWTSecrets;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 *
 * If authentication is successful an Authentication is returned.
 * If authentication cannot be determined, an empty Mono is returned.
 * If authentication fails, a Mono error is returned.
 */
@Slf4j
@RequiredArgsConstructor
public class BearerTokenAuthenticationManager implements
    ReactiveAuthenticationManager {

  private final JWTSecrets secrets;

  private JwtParser parser;

  @PostConstruct
  public void init() {
    parser = Jwts.parser()
        .verifyWith(secrets.getKey())
        .build();
  }

  @Override
  public Mono<Authentication> authenticate(Authentication auth) {

    if (!(auth instanceof BearerTokenAuthenticationToken))
      return Mono.empty();

    var token_auth = (BearerTokenAuthenticationToken) auth;
    var token = token_auth.getToken();

    log.debug("Парсинг токена: {}", token);

    var claims = parser.parseSignedClaims(token)
        .getPayload();

    // {iss=seabattle_service, sub=test22, user_id=1, roles=Player, iat=1733872306,
    // exp=1733908306}

    // new OAuth2AccessToken(TokenType.BEARER, "", , )
    // parser.apply(token_auth.getToken()).subscribe(c -> log.info("parsed claims
    // {}", c));

    var role = new SimpleGrantedAuthority(claims.get("roles", String.class));

    var principal = new DefaultOAuth2AuthenticatedPrincipal(claims.getSubject(), claims, List.of(role));

    var result = new BearerTokenAuthentication(principal, parseToken(token, claims), List.of(role));
    return Mono.just(result);

  }

  private OAuth2AccessToken parseToken(String token, Claims claims) {
    // TODO ловить exception?

    long iat = claims.get("iat", Long.class);
    long exp = claims.get("exp", Long.class);

    Instant iat_instant = Instant.ofEpochMilli(iat);
    Instant exp_instant = Instant.ofEpochMilli(exp);

    return new OAuth2AccessToken(TokenType.BEARER, token, iat_instant, exp_instant);
  }

}
