package org.ssau.sandbox.auth;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Component
@Slf4j
public class UsernamePasswordAuthenticationManager implements ReactiveAuthenticationManager {

  private final UserDetailsService userDetailsService;
  private final PasswordEncoder encoder;

  @Override
  public Mono<Authentication> authenticate(Authentication authentication) {

    if (!(authentication instanceof UsernamePasswordAuthenticationToken))
      throw new IllegalArgumentException("Unsupported authentication");

    log.info("Authentication is {}", authentication.getClass());
    String password = (String) authentication.getCredentials();

    return userDetailsService.findByUsername(authentication.getName())
        .switchIfEmpty(Mono.error(new BadCredentialsException("User not found")))
        .flatMap(userDetails -> {
          if (encoder.matches(password, userDetails.getPassword()))
            return Mono
                .just(new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities()));
          else
            return Mono.error(new BadCredentialsException("Invalid password"));
        });

  }

}
