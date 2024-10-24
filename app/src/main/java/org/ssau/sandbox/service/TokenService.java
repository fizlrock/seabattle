package org.ssau.sandbox.service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.ssau.sandbox.domain.AppUser;

import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * SecurityService
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class TokenService {

  @Value("${jwt.secret}")
  private String key_base64;
  @Value("${jwt.experation}")
  private Integer expInSeconds;
  @Value("${jwt.issuer}")
  private String issuer;

  private SecretKey key;

  @PostConstruct
  void init() {
    byte[] decoded_key = Base64.getDecoder().decode(key_base64);

    key = new SecretKey() {
      @Override
      public String getAlgorithm() {
        return "HmacSHA256";
      }

      @Override
      public byte[] getEncoded() {
        return decoded_key;
      }

      @Override
      public String getFormat() {
        return "RAW";
      }
    };
  }

  private static Function<Principal, AppUser> toAppUser = p -> (AppUser) (((UsernamePasswordAuthenticationToken) p)
      .getPrincipal());

  public String getToken(Principal p) {

    LocalDateTime issuedAt = LocalDateTime.now();
    LocalDateTime expAt = issuedAt.plusSeconds(expInSeconds);

    var user = toAppUser.apply(p);
    log.info("Token generated for {} with role {}", user.getUsername(), user.getRole());

    return Jwts.builder()
        .issuer(issuer)
        .claim("id", user.getId())
        .claim("role", user.getRole().toString())
        .issuedAt(Date.from(issuedAt.toInstant(ZoneOffset.UTC)))
        .expiration(Date.from(expAt.toInstant(ZoneOffset.UTC)))
        .subject(user.getUsername())
        .signWith(key)
        .compact();
  }

}
