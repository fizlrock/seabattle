package org.ssau.sandbox.auth.jwt;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JWTTokenService {

  private final JWTSecrets secrets;

  /**
   * Create and sign a JWT object using information from the current
   * authenticated principal
   *
   * @param subject     Name of current principal
   * @param credentials Credentials of current principal
   * @param authorities A collection of granted authorities for this principal
   * @return String representing a valid token
   */
  public String generateToken(String subject,
      Collection<? extends GrantedAuthority> authorities) {

    LocalDateTime issuedAt = LocalDateTime.now();
    LocalDateTime expAt = issuedAt.plusSeconds(secrets.getExpInSeconds());

    return Jwts.builder()
        .issuer(secrets.getIssuer())
        .subject(subject)
        .claim("roles",
            authorities.stream()
                .map(GrantedAuthority.class::cast)
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(",")))
        .issuedAt(Date.from(issuedAt.toInstant(ZoneOffset.UTC)))
        .expiration(Date.from(expAt.toInstant(ZoneOffset.UTC)))
        .subject(subject)
        .signWith(secrets.getKey())
        .compact();
  }

}
