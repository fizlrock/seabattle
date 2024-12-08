package org.ssau.sandbox.auth.filter;

import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.ssau.sandbox.auth.UsernamePasswordAuthenticationManager;
import org.ssau.sandbox.auth.basic.BasicAuthenticationConverter;
import org.ssau.sandbox.auth.basic.BasicAuthenticationSuccessHandler;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

/**
 * BasicAuthenticationFilter
 */
@Slf4j
public class BasicAuthenticationFilter extends AuthenticationWebFilter {

  @PostConstruct
  void init() {
    log.warn("basic auth filter created");
  }

  /**
   * Класс вытаскивает данные аутентификации из запроса
   */

  public BasicAuthenticationFilter(
      UsernamePasswordAuthenticationManager authManager,
      BasicAuthenticationSuccessHandler handler,
      BasicAuthenticationConverter converter) {

    super(authManager);

    log.info("Инициализация BasicAuth Filter. Обработчик успешной авторизации: {}, Auth manager: {}", handler,
        authManager);

    this.setAuthenticationSuccessHandler(handler);
    this.setServerAuthenticationConverter(converter);

  }

}
