package org.ssau.seabattle.configuration;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.ssau.seabattle.repository.UserRepository;

import lombok.RequiredArgsConstructor;

/**
 * SecurityService
 */
@Component
@RequiredArgsConstructor
public class SecurityService {
  private final UserRepository userRepo;
  private final PasswordEncoder encoder;

}
