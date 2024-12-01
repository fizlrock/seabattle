package org.ssau.sandbox.auth;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.ssau.sandbox.repository.AppUserRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Component
public class BasicHttpReactiveAuthenticationMananger implements ReactiveAuthenticationManager {

  private final UserDetailsService userDetailsService;
  private final PasswordEncoder encoder;

  @Override
  public Mono<Authentication> authenticate(Authentication authentication) {

    String password = (String) authentication.getCredentials();

    return userDetailsService.findByUsername(authentication.getName())

        .flatMap(userDetails -> {
          if (encoder.matches(password, userDetails.getPassword())) {
            return Mono
                .just(new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities()));
          } else {
            return Mono.error(new BadCredentialsException("Invalid credentials"));
          }
        });

  }

}
