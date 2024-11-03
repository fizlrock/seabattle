package org.ssau.sandbox.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
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

  // @Bean
  // public SecurityWebFilterChain springSecurityFilterChain(
  // ServerHttpSecurity http,
  // BasicAuthenticationFilter basicAuthFilter,
  // BearerAuthenticationFilter bearerAuthFilter) {

  // http
  // .authorizeExchange(e -> e
  // .pathMatchers("/token", "/")
  // .authenticated())
  // .addFilterAt(basicAuthFilter, SecurityWebFiltersOrder.HTTP_BASIC)
  // .authorizeExchange(e -> e
  // .pathMatchers("/api/**")
  // .authenticated())
  // .addFilterAt(bearerAuthFilter, SecurityWebFiltersOrder.AUTHENTICATION)
  // .csrf().disable();

  // return http.build();
  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration corsConfig = new CorsConfiguration();
    corsConfig.setAllowedOrigins(List.of("*")); // Allow from any origin
    corsConfig.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH")); // Allowed HTTP methods
    corsConfig.setAllowedHeaders(List.of("*")); // Allowed headers
    // corsConfig.setAllowCredentials(true); // Allow cookies
    // corsConfig.setMaxAge(3600L); // Set max age in seconds

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", corsConfig); // Apply to all paths

    return source;
  } // }

  @Bean
  public SecurityWebFilterChain springSecurityFilterChain(
      ServerHttpSecurity http,
      BasicAuthenticationFilter basicAuthFilter,
      BearerAuthenticationFilter bearerAuthFilter,
      CorsConfigurationSource corsConfig) {
    return http
        // .authorizeExchange(e -> e.anyExchange().permitAll())
        .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
        .csrf().disable()

        .cors(c -> c.configurationSource(corsConfig))

        .authorizeExchange(e -> e
            .pathMatchers("/token", "/")
            .authenticated())

        .authorizeExchange(e -> e
            .pathMatchers(HttpMethod.POST, "/user")
            .permitAll())

        .addFilterAt(basicAuthFilter, SecurityWebFiltersOrder.HTTP_BASIC)
        .authorizeExchange(e -> e
            .pathMatchers("/user/**")
            .authenticated())
        .addFilterAt(bearerAuthFilter, SecurityWebFiltersOrder.AUTHENTICATION)
        .build();

  }

}
