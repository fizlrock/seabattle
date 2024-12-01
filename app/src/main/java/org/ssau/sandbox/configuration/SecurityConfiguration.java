package org.ssau.sandbox.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher;
import org.ssau.sandbox.auth.BasicHttpReactiveAuthenticationMananger;
import org.ssau.sandbox.auth.basic.BasicAuthenticationSuccessHandler;
import org.ssau.sandbox.auth.filter.BasicAuthenticationFilter;

import lombok.extern.slf4j.Slf4j;

/**
 * SecurityConfiguration
 */
@Configuration
@EnableWebFluxSecurity
@Slf4j
public class SecurityConfiguration {

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  @Order(Ordered.LOWEST_PRECEDENCE)
  SecurityWebFilterChain getHttpBearerSecurity(
      ServerHttpSecurity http) {
    http
        .csrf(csrf -> csrf.disable())
        .cors(cors -> cors.disable())
        .httpBasic(basic -> basic.disable())
        .formLogin(form -> form.disable())
        .logout(logout -> logout.disable())
        .securityContextRepository(NoOpServerSecurityContextRepository.getInstance()) // Отключение сессий

        .authorizeExchange(e -> e.pathMatchers(HttpMethod.POST, "/user").permitAll())

        .authorizeExchange(e -> e.anyExchange().authenticated());

    return http.build();
  }

  @Bean
  @Order(Ordered.HIGHEST_PRECEDENCE)
  SecurityWebFilterChain getHttpAuthSecurity(
      ServerHttpSecurity http,
      BasicHttpReactiveAuthenticationMananger authManager,
      BasicAuthenticationSuccessHandler handler) {

    var basic_auth_filter = new BasicAuthenticationFilter(authManager, handler);

    var matcher = new PathPatternParserServerWebExchangeMatcher("/token", HttpMethod.GET);

    http
        .securityMatcher(matcher)
        .csrf(csrf -> csrf.disable())
        .cors(cors -> cors.disable())
        .httpBasic(basic -> basic.disable())
        .formLogin(form -> form.disable())
        .logout(logout -> logout.disable())
        .securityContextRepository(NoOpServerSecurityContextRepository.getInstance()) // Отключение сессий
        .authorizeExchange(e -> e
            .anyExchange().permitAll())
        .addFilterAt(basic_auth_filter, SecurityWebFiltersOrder.AUTHENTICATION);

    return http.build();
  }

}
