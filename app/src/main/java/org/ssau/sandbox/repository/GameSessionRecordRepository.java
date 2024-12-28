package org.ssau.sandbox.repository;

import java.util.UUID;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import org.ssau.sandbox.domain.game.GameSessionRecord;
import org.ssau.sandbox.domain.user.Avatar;

/**
 * GameSessionRecordRepository
 */
@Repository
public interface GameSessionRecordRepository extends ReactiveCrudRepository<GameSessionRecord, UUID>{

}
