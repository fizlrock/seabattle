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
    if (!header.startsWith("Bearer "))
      throw new BadCredentialsException("Токен необходимо передавать в формате Bearer <сам токен>");
    return header.substring(token_start_index);
  }

  public static Mono<String> extract(ServerWebExchange exchange) {
    log.info("Попытка вытащить токен из запроса: {}", exchange.getRequest().getPath());

    // TODO эта реализция ужасна
    return Mono.justOrEmpty(exchange)
        .map(ServerWebExchange::getRequest)
        .map(ServerHttpRequest::getHeaders)
        .map(h -> h.get(HttpHeaders.AUTHORIZATION))
        .map(l -> l.get(0)) // TODO
        .map(HeadersBearerTokenExtractor::validateAndExtract)
        .onErrorResume(e -> {
          log.warn("Ошибка парсинга токена: {}", e.toString());
          return Mono.empty();
        });

  }
}
