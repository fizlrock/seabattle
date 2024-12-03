package org.ssau.sandbox.configuration;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher;
import org.springframework.web.cors.CorsConfiguration;
import static org.springframework.web.cors.CorsConfiguration.ALL;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.server.ServerWebExchange;
import org.ssau.sandbox.auth.BasicHttpReactiveAuthenticationMananger;
import org.ssau.sandbox.auth.basic.BasicAuthenticationSuccessHandler;
import org.ssau.sandbox.auth.bearer.ServerHttpBearerAuthenticationConverter;
import org.ssau.sandbox.auth.filter.BasicAuthenticationFilter;
import org.ssau.sandbox.auth.filter.BearerAuthenticationFilter;

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
  CorsConfigurationSource getCors() {

    var corsConfigurationSource = new UrlBasedCorsConfigurationSource();
    // Конфигурация CORS
    var globalCorsConfiguration = new CorsConfiguration();

    // Разрешаются CORS-запросы:
    // - с сайта http://localhost:8080

    globalCorsConfiguration.addAllowedOrigin(ALL);
    globalCorsConfiguration.addAllowedHeader(ALL);
    // globalCorsConfiguration.addAllowedMethod(ALL);

    globalCorsConfiguration.setAllowedMethods(List.of(
        HttpMethod.GET.name(),
        HttpMethod.POST.name(),
        HttpMethod.PUT.name(),
        HttpMethod.PATCH.name(),
        HttpMethod.DELETE.name()));

    // Использование конфигурации CORS для всех запросов
    corsConfigurationSource.registerCorsConfiguration("/**", globalCorsConfiguration);

    return corsConfigurationSource;
  }

  @Bean
  @Order(Ordered.LOWEST_PRECEDENCE)
  SecurityWebFilterChain getHttpBearerSecurity(
      ServerHttpSecurity http,
      ServerHttpBearerAuthenticationConverter converter,

      CorsConfigurationSource corsConfig) {

    var jwt_filter = new BearerAuthenticationFilter(converter);

    http
        .csrf(csrf -> csrf.disable())
        .cors(cors -> cors.configurationSource(corsConfig)) // TODO
        .httpBasic(basic -> basic.disable())
        .formLogin(form -> form.disable())
        .logout(logout -> logout.disable())
        .securityContextRepository(NoOpServerSecurityContextRepository.getInstance()) // Отключение сессий

        .authorizeExchange(e -> e.pathMatchers(HttpMethod.POST, "/user").permitAll())

        .authorizeExchange(e -> e.pathMatchers(HttpMethod.GET, "/user/profile").authenticated())
        .addFilterAt(jwt_filter, SecurityWebFiltersOrder.AUTHENTICATION);

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
