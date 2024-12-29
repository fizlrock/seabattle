package org.ssau.sandbox.repository;

import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;

import org.openapitools.model.GameStatsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import reactor.core.publisher.Flux;

/**
 * GameSessionRecordRepository
 */
@Repository
public class GameStatsRepository {

  @Autowired
  DatabaseClient client;

  String req = "SELECT ROW_NUMBER() OVER (ORDER BY gsr.ended_at ASC) AS game_number, au.username AS opponent_name, CASE WHEN gsr.winner_player_id = %d THEN TRUE ELSE FALSE END AS you_win, ROUND(EXTRACT(EPOCH FROM (gsr.ended_at - gsr.started_at))) AS duration FROM game_session_record gsr JOIN app_user au ON au.id = CASE WHEN gsr.second_player_id = %d THEN gsr.first_player_id ELSE gsr.second_player_id END WHERE gsr.first_player_id = %d OR gsr.second_player_id = %d ORDER BY gsr.ended_at DESC LIMIT %d;";

  private String formatRequest(Long userId, Long count) {
    return req.formatted(userId, userId, userId, userId, count);
  }

  public Flux<GameStatsDto> getPersonalStat(Long userId, Long count) {

    String request = formatRequest(userId, count);

    return client.sql(request)
        .map(this::mapToObject)
        .all();
  }

  private GameStatsDto mapToObject(Row row, RowMetadata meta) {

    Long game_number = (Long) row.get("game_number");
    String opponent_name = (String) row.get("opponent_name");
    boolean you_win = (boolean) row.get("you_win");
    BigDecimal duration = (BigDecimal) row.get("duration");
    var dto = new GameStatsDto();


    dto.youWin(you_win);
    dto.setOpponentName(opponent_name);
    dto.setDuration(duration.intValue());
    dto.setHitPercentage(ThreadLocalRandom.current().nextInt(20, 40));
    dto.setNumber(game_number.intValue());


    return dto;

  }
}
