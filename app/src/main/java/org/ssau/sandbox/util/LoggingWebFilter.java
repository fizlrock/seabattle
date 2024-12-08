package org.ssau.sandbox.util;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import reactor.core.publisher.Mono;

@Component
public class LoggingWebFilter implements WebFilter {

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
    System.out.println("Request Path: " + exchange.getRequest().getPath());
    System.out.println("Filters are executing...");

    return chain.filter(exchange)
        .doOnSuccess(aVoid -> System.out.println("Filters completed for: " + exchange.getRequest().getPath()));
  }
}
