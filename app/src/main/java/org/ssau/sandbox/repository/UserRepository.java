package org.ssau.sandbox.repository;

import java.util.Optional;

import org.ssau.sandbox.domain.AppUser;

import reactor.core.publisher.Mono;

/**
 * UserRepository
 */
public interface UserRepository {
  public Mono<AppUser> findByUsername(String username);

  public void saveEntity(AppUser user);

}
