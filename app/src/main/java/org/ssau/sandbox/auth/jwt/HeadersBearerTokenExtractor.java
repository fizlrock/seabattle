package org.ssau.sandbox.auth.jwt;

import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.server.ServerWebExchange;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
public class HeadersBearerTokenExtractor {

  private static final int token_start_index = "Bearer ".length();

  private static String validateAndExtract(String header) {
    header = header.strip();

    log.warn("FUUUUCCK {} ", header);

    if (!header.startsWith("Bearer "))
      throw new BadCredentialsException("Токен необходимо передавать в формате Bearer <сам токен>");

    return header.substring(token_start_index);
  }

  public static Mono<String> extract(ServerWebExchange exchange) {

    // TODO эта реализция ужасна

    return Mono.justOrEmpty(exchange)
        .map(ServerWebExchange::getRequest)
        .map(ServerHttpRequest::getHeaders)
        .map(h -> h.get(HttpHeaders.AUTHORIZATION))
        .map(l -> l.getFirst()) // TODO тут должна быть обработка ошибок в случае если запрос не содержал
        .onErrorResume(e -> {
          return Mono.empty();
        })
        // заголовка Authorization
        .map(HeadersBearerTokenExtractor::validateAndExtract);

  }
}
