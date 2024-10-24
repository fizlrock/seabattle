package org.ssau.sandbox.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.security.web.server.csrf.CookieServerCsrfTokenRepository;
import org.ssau.sandbox.repository.UserRepository;

@EnableWebFluxSecurity
@Configuration
public class SecurityConfiguration {

  @Bean
  public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
    return http
        .authorizeExchange(e -> e
            .pathMatchers("/private")
            .authenticated())
        .authorizeExchange(e -> e
            .pathMatchers("/share")
            .permitAll())
        .authorizeExchange(e -> e
            .pathMatchers("/api/token")
            .permitAll())
        .authorizeExchange(e -> e
            .pathMatchers("/login")
            .permitAll())

        .securityContextRepository(NoOpServerSecurityContextRepository.getInstance()) // Отключаем сессии
        .csrf(csrf -> csrf.csrfTokenRepository(new CookieServerCsrfTokenRepository()))
        .httpBasic(Customizer.withDefaults())
        .build();
  }

  @Bean
  public PasswordEncoder getPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public ReactiveUserDetailsService userDetailsService(UserRepository repa) {
    return (username) -> repa.findByUsername(username).map(u -> (UserDetails) u);
  }
}
