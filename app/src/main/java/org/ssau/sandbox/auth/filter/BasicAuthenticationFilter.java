package org.ssau.sandbox.auth.filter;

import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.ssau.sandbox.auth.basic.BasicAuthenticationSuccessHandler;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

/**
 * BasicAuthenticationFilter
 */
@Slf4j
public class BasicAuthenticationFilter extends AuthenticationWebFilter {


  @PostConstruct
  void init(){
    log.warn("basic auth filter created");
  }

  public BasicAuthenticationFilter(
      ReactiveAuthenticationManager authManager,
      BasicAuthenticationSuccessHandler handler) {

    super(authManager);
    this.setAuthenticationSuccessHandler(handler);
    // this.setRequiresAuthenticationMatcher(ServerWebExchangeMatchers.pathMatchers("/login"));
  }

}
