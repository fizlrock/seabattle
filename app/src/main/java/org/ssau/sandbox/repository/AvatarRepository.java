package org.ssau.sandbox.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import org.ssau.sandbox.domain.user.Avatar;

@Repository
public interface AvatarRepository extends ReactiveCrudRepository<Avatar, Long> {

}
