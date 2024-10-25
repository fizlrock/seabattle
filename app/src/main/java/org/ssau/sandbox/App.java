/*
 * Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.ssau.sandbox;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
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
 * A Spring RESTful Application showing authentication and authorization
 * Signed Commit
 * 
 * @author rafa
 *
 **/
@SpringBootApplication
@EnableWebFluxSecurity
public class App {

  public static void main(String[] args) {
    SpringApplication.run(App.class, args);
  }

  @Bean
  public MapReactiveUserDetailsService userDetailsRepository(PasswordEncoder encoder) {
    UserDetails user = User.builder()
        .username("user")
        .password(encoder.encode("user"))
        .roles("USER", "ADMIN")
        .build();
    return new MapReactiveUserDetailsService(user);
  }
  
  @Bean
  public UserDetailsRepositoryReactiveAuthenticationManager basicAuthManager(
      MapReactiveUserDetailsService userDetailsService) {
    return new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService);
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
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
