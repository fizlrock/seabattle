package org.ssau.sandbox.auth.jwt;

import org.springframework.http.HttpHeaders;
import org.springframework.web.server.ServerWebExchange;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
public class AuthorizationHeaderPayload {

    public static Mono<String> extract(ServerWebExchange serverWebExchange) {
        log.info("Вытаскиваем токен");

        return Mono.justOrEmpty(serverWebExchange.getRequest()
                .getHeaders()
                .getFirst(HttpHeaders.AUTHORIZATION));
    }
}
