package org.ssau.seabattle.repository;

import org.springframework.data.repository.Repository;
import org.springframework.stereotype.Component;
import org.ssau.seabattle.userModel.UserEntity;

import reactor.core.publisher.Mono;

@Component
// TODO здесь должен быть реактивный репозиторий
public interface UserRepository extends Repository<UserEntity, Long> {

  UserEntity findByUsername(String username);

}
