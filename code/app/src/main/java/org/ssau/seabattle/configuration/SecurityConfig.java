package org.ssau.seabattle.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

  @Bean
  PasswordEncoder getPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }

  // @Bean
  // public SecurityFilterChain securityFilterChain(HttpSecurity http) throws
  // Exception {
  // http
  // .authorizeHttpRequests((authorize) -> authorize
  // .anyRequest().authenticated())
  // .httpBasic(Customizer.withDefaults())
  // .formLogin(Customizer.withDefaults());

  // return http.build();
  // }

  // @Bean
  // public UserDetailsService userDetailsService() {
  // UserDetails userDetails = User.withDefaultPasswordEncoder()
  // .username("user")
  // .password("password")
  // .roles("USER")
  // .build();

  // return new InMemoryUserDetailsManager(userDetails);
  // }
}
