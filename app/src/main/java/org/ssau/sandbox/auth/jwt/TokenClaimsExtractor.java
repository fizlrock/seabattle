package org.ssau.sandbox.auth.jwt;

import java.util.Date;
import java.util.function.Function;
import java.util.function.Predicate;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class TokenClaimsExtractor implements Function<String, Mono<Claims>> {

  private final JWTSecrets secrets;

  private JwtParser parser;

  void init() {
    // TODO может в отдельный компонент вынести?
    parser = Jwts.parser()
        .verifyWith(secrets.getKey())
        .build();
  }

  private Predicate<Claims> notExpired = claims -> claims.getExpiration().before(new Date());

  @Override
  public Mono<Claims> apply(String token) {
    return Mono.just(token)
        .map(parser::parseSignedClaims)
        .map(Jws::getPayload)
        .filter(notExpired)
        .log();
  }

}
