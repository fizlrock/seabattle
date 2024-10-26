package org.ssau.sandbox.configuration;

import org.springframework.context.annotation.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.ssau.sandbox.auth.filter.BasicAuthenticationFilter;
import org.ssau.sandbox.auth.filter.BearerAuthenticationFilter;

/**
 * SecurityConfiguration
 */
@Configuration
@EnableWebFluxSecurity
public class SecurityConfiguration {

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public MapReactiveUserDetailsService userDetailsRepository() {
    UserDetails user = User
        .withDefaultPasswordEncoder()
        .username("user")
        .password("user")
        .roles("USER", "ADMIN")
        .build();

    return new MapReactiveUserDetailsService(user);
  }

  @Bean
  public UserDetailsRepositoryReactiveAuthenticationManager basicAuthManager(
      MapReactiveUserDetailsService userDetailsService) {
    return new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService) {
      @Override
      public String toString() {
        return "basicAuthAuthenticationManager";
      }

    };
  }

  @Bean
  public SecurityWebFilterChain springSecurityFilterChain(
      ServerHttpSecurity http,
      BasicAuthenticationFilter basicAuthFilter,
      BearerAuthenticationFilter bearerAuthFilter) {

    http
        .authorizeExchange(e -> e
            .pathMatchers("/login", "/")
            .authenticated())
        .addFilterAt(basicAuthFilter, SecurityWebFiltersOrder.HTTP_BASIC)
        .authorizeExchange(e -> e
            .pathMatchers("/api/**")
            .authenticated())
        .addFilterAt(bearerAuthFilter, SecurityWebFiltersOrder.AUTHENTICATION);

    return http.build();
  }

}
