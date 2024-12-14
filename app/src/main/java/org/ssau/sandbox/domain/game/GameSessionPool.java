package org.ssau.sandbox.domain.game;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.ssau.sandbox.domain.game.GameSession.GameState;

import io.netty.util.internal.ConcurrentSet;
import lombok.extern.slf4j.Slf4j;

/**
 * Структура хранит активные игры
 * GameSessionPool
 */
@Component
@Slf4j
public class GameSessionPool {

  public final Map<GameSession, Object> sessions = new ConcurrentHashMap<>();

  /**
   * Получить статистику пула игровых сессий
   * 
   * @return словарь состояние:кол-во
   */
  private Map<GameState, Long> getPoolStateStat() {
    return sessions.entrySet().stream().map(Entry::getKey)
        .collect(Collectors.groupingBy(GameSession::getState, Collectors.counting()));
  }

  private void logPoolState() {
    log.debug("Размер пула игр: {}", sessions.size());
    log.debug("Статистика состояний: {}", getPoolStateStat());
  }

  public final GameSettings settings = new GameSettings(
      new GameMapSettings(10, 10),
      List.of(
          new BoatType(1, 4),
          new BoatType(2, 3),
          new BoatType(3, 2),
          new BoatType(4, 1)));

  private GameSession createNewSession() {
    var session = new GameSession(settings);
    sessions.put(session, null);
    logPoolState();
    return session;
  }

  public GameSession findSession() {
    log.debug("Поиск игровой сессии");
    var session = sessions.entrySet().stream().map(Entry::getKey)
        .filter(s -> s.getState() == GameState.WaitingSecondPlayer)
        .findAny();

    return session.orElseGet(this::createNewSession);
  }

  /**
  * 
  */
  public void filterSessions() {

  }

}
