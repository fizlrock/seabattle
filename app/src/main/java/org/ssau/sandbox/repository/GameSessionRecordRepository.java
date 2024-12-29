package org.ssau.sandbox.repository;

import java.util.UUID;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.ssau.sandbox.domain.game.GameSessionRecord;


public interface GameSessionRecordRepository extends ReactiveCrudRepository<GameSessionRecord, UUID> {

	
}
