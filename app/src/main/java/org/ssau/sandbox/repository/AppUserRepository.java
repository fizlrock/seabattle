package org.ssau.sandbox.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import org.ssau.sandbox.domain.AppUser;

import reactor.core.publisher.Mono;

@Repository
public interface AppUserRepository extends ReactiveCrudRepository<AppUser, Long> {
  public Mono<AppUser> findByUsername(String username);

}
