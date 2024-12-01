package org.ssau.sandbox.auth.jwt;

import org.springframework.web.server.ServerWebExchange;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
public class AuthorizationHeaderPayload {

  public static Mono<String> extract(ServerWebExchange exchange) {
    log.info("Содержимое кук: {}", exchange.getRequest().getCookies().getFirst("Bearer"));

    return Mono.justOrEmpty(exchange)
        .map(ServerWebExchange::getRequest)
        .map(request -> request.getCookies())
        .map(cookies -> cookies.getFirst("Bearer"))
        .map(cookie -> cookie.getValue())

    ;
  }
}
