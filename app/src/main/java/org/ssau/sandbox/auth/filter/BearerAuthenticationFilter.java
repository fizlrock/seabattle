package org.ssau.sandbox.auth.filter;

import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.ssau.sandbox.auth.bearer.ServerHttpBearerAuthenticationConverter;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class BearerAuthenticationFilter extends AuthenticationWebFilter {

  @PostConstruct
  void init() {
    log.warn("bearer filter  created");
  }

  private ServerAuthenticationSuccessHandler successHandler = new ServerAuthenticationSuccessHandler() {

    @Override
    public Mono<Void> onAuthenticationSuccess(WebFilterExchange exchange, Authentication auth) {
      log.info("Пользователь был успешно аутентифицирован через Bearer: {}", auth);
      return exchange
          .getChain()
          .filter(exchange.getExchange());
    }

  };
  private static ReactiveAuthenticationManager authenticationManager = x -> Mono.just(x);

  public BearerAuthenticationFilter(
      ServerHttpBearerAuthenticationConverter converter) {
    super(authenticationManager);
    this.setServerAuthenticationConverter(converter);
    this.setAuthenticationSuccessHandler(successHandler);
    // this.setRequiresAuthenticationMatcher(ServerWebExchangeMatchers.pathMatchers("/user/**"));
  }

}
