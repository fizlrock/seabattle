package org.ssau.sandbox.domain.game;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.ssau.sandbox.domain.game.GameSession.GameState;

import lombok.extern.slf4j.Slf4j;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;

/**
 * Структура хранит активные игры
 * GameSessionPool
 */
@Component
@Slf4j
public class GameSessionPool {

  public final Map<GameSession, Object> sessions = new ConcurrentHashMap<>();

  // TODO после завершения игры, нужно удалить объекты из sessions и
  // userSessionDict
  public final Map<Long, GameSession> userSessionDict = new ConcurrentHashMap<>();

  public final GameSettings settings = new GameSettings(
      new GameMapSettings(10, 10),
      List.of(
          new BoatType(1, 4),
          new BoatType(2, 3),
          new BoatType(3, 2),
          new BoatType(4, 1)));

  /**
   * Получить не начатую игровую сессию.
   * Это будет или новая сессия. Или сессия ожидающая второго игрока
   * 
   * @return
   */
  public GameSession findSession() {
    log.debug("Поиск игровой сессии");
    var session = sessions.entrySet().stream().map(Entry::getKey)
        .filter(s -> s.getState() == GameState.WaitingSecondPlayer)
        .findAny();

    return session.orElseGet(this::createNewSession);
  }

  public Optional<GameSession> findUserSession(long user_id) {
    // TODO наверное стоит это оптимизировать

    Predicate<GameSession> userInSession = s -> {
      if (s.getState() == GameState.WaitingSecondPlayer)
        return s.getFirstPlayerId() == user_id;
      else
        return (s.getFirstPlayerId() == user_id) || (s.getSecondPlayerId() == user_id);
    };

    return sessions.entrySet().stream()
        .map(Entry::getKey)
        .filter(userInSession)
        .findAny();
  }

  /**
   * Получить статистику пула игровых сессий
   * 
   * @return словарь состояние:кол-во
   */
  private Map<GameState, Long> getPoolStateStat() {
    return sessions.entrySet().stream().map(Entry::getKey)
        .collect(Collectors.groupingBy(GameSession::getState, Collectors.counting()));
  }

  // Логирование раз в 10 секунд
  // private Disposable f = Flux.interval(Duration.ofSeconds(10))
  // .subscribe(x -> logPoolState());

  private void logPoolState() {
    log.info("Размер пула игр: {}", sessions.size());
    log.info("Статистика состояний: {}", getPoolStateStat());
    log.info("Статистика состояний: {}", sessions);
  }

  private GameSession createNewSession() {
    var session = newSession();
    sessions.put(session, new Object());
    logPoolState();
    return session;
  }


  @Autowired
  private ApplicationContext applicationContext;

  private GameSession newSession() {
    return applicationContext.getBean(GameSession.class, settings);
  }

}
