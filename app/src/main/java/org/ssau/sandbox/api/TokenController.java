package org.ssau.sandbox.api;

import org.openapitools.api.TokenApi;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

/**
 * TokenController
 */
@RestController
public class TokenController implements TokenApi {

  @Override
  public Mono<Void> getToken(ServerWebExchange exchange) {
    return Mono.empty();
  }

}
