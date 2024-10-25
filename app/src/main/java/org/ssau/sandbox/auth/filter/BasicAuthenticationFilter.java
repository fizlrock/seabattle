package org.ssau.sandbox.auth.filter;

import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.stereotype.Component;
import org.ssau.sandbox.auth.basic.BasicAuthenticationSuccessHandler;

/**
 * BasicAuthenticationFilter
 */
@Component
public class BasicAuthenticationFilter extends AuthenticationWebFilter {

  public BasicAuthenticationFilter(
      UserDetailsRepositoryReactiveAuthenticationManager authManager,
      BasicAuthenticationSuccessHandler handler) {

    super(authManager);
    this.setAuthenticationSuccessHandler(handler);
  }

}
