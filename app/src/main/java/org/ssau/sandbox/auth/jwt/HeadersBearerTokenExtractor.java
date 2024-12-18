package org.ssau.sandbox.auth.jwt;

import org.springframework.http.HttpHeaders;
import org.springframework.web.server.ServerWebExchange;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
public class HeadersBearerTokenExtractor {

  private static final int token_start_index = "Bearer ".length();

  public static Mono<String> extract(ServerWebExchange exchange) {
    log.debug("Попытка вытащить токен из запроса: {}", exchange.getRequest().getPath());

    var headers = exchange.getRequest().getHeaders();

    var auth_headers = headers.get(HttpHeaders.AUTHORIZATION);

    if (auth_headers == null || !auth_headers.get(0).startsWith("Bearer "))
      return Mono.empty();
    var token = auth_headers.get(0).substring(token_start_index);

    return Mono.just(token);

  }
}
