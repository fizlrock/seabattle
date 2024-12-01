package org.ssau.sandbox.auth.jwt;

import java.util.Date;
import java.util.function.Function;
import java.util.function.Predicate;

import org.springframework.stereotype.Component;
import org.ssau.sandbox.auth.exception.TokenExpiredException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * Функция проверяет
 * <ol>
 * <li>Подпись
 * <li>Дату действия
 * </ol>
 * и возвращет Claims
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class TokenClaimsExtractor implements Function<String, Mono<Claims>> {

  private final JWTSecrets secrets;

  private JwtParser parser;

  private Predicate<Claims> notExpired = claims -> claims.getExpiration().after(new Date());

  @PostConstruct
  void init() {
    parser = Jwts.parser()
        .verifyWith(secrets.getKey())
        .build();
  }

  @Override
  public Mono<Claims> apply(String token) {
    log.info("Попытка распарсить токен: {}", token);
    return Mono.just(token)
        .map(parser::parseSignedClaims)
        .map(Jws::getPayload)
        .filter(notExpired)
        .switchIfEmpty(Mono.error(new TokenExpiredException()));
  }

}
