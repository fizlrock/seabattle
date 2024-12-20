package org.ssau.sandbox.domain.game;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.ssau.sandbox.domain.game.field.GameField;
import org.ssau.sandbox.service.WaitService;

import jakarta.annotation.PostConstruct;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * Содержит логику изменения состояния игры и проверку допустимости действий
 * Сессия игры в морской бой
 * SeabattleGame
 */
@Slf4j
@ToString
@Component
@Scope("prototype")
public class GameSession {

  public static enum GameState {
    Created, WaitingSecondPlayer, Ended, Failed, Started
  }

  private final UUID sessionId;

  private long version = 0;

  private final GameField firstPlayerField;
  private final GameField secondPlayerField;

  private Long firstPlayerId;
  private Long secondPlayerId;

  Disposable failTask;


  @PostConstruct
  void fuck(){
    System.out.println("\nFUUUUUUUUUUCK YOOUT\n");
  }

  // @Value("${seabattle.matchmaking_timeout}")
  long matchmakingTimeout = 120;

  // @Value("${seabattle.fire_timeout}")
  long fireTimeout = 60; // TODO @Value почему-то ломает инициализцию бина

  @Autowired
  private WaitService waitService;

  /**
   * Идентификатор активного игрока.
   * <p>
   * В случае когда {@link GameState} == Ended.
   * Так же является идентификатором победителя
   */
  private Long activePlayerId;

  /**
   * Дата создания игровой сессии
   */
  private final LocalDateTime createdAt = LocalDateTime.now();

  /**
   * Дата начала игры. (Присоединения двух игроков)
   */
  private LocalDateTime startedAt;

  private LocalDateTime endedAt;

  private long lastShotMillis = 0;

  /**
   * Настройки игрового поля
   */
  private final GameSettings settings;


  private GameState state;

  /**
   * Максимальный счет в игре. Иными словами, число палуб в начале.
   */
  private final long max_score;

  public GameSession(GameSettings settings) {
    sessionId = UUID.randomUUID();
    log.debug("Создание новой игровой сессии.ID: {}", sessionId);
    this.settings = settings;

    firstPlayerField = new GameField(settings);
    secondPlayerField = new GameField(settings);

    max_score = settings.boatTypes().stream().mapToInt(t -> t.size() * t.count()).sum();

    state = GameState.Created;
    planMatchmakingTimeoutTask();
  }

  private void planMatchmakingTimeoutTask() {

    if (failTask != null && !failTask.isDisposed())
      failTask.dispose();

    failTask = Mono.delay(Duration.ofSeconds(matchmakingTimeout))
        .doOnNext(tick -> fail("Team building timeout"))
        .subscribeOn(Schedulers.boundedElastic()).subscribe();
  }

  private void planFireTimeoutTask() {
    if (failTask != null && !failTask.isDisposed())
      failTask.dispose();

    failTask = Mono.delay(Duration.ofSeconds(fireTimeout))
        .doOnNext(tick -> fail("Fire timeout"))
        .subscribeOn(Schedulers.boundedElastic()).subscribe();
  }

  public Long getFirstPlayerId() {
    return firstPlayerId;
  }

  public Long getSecondPlayerId() {
    return secondPlayerId;
  }

  public Long getActivePlayerId() {
    return activePlayerId;
  }

  public UUID getSessionId() {
    return sessionId;
  }

  public long getVersion() {
    return version;
  }

  public GameField getFirstPlayerField() {
    return firstPlayerField;
  }

  public GameField getSecondPlayerField() {
    return secondPlayerField;
  }

  public GameSettings getSettings() {
    return settings;
  }

  public GameState getState() {
    return state;
  }

  public void addPlayer(Long playerId, Collection<BoatCord> cords) {
    GameField field = null;

    switch (state) {
      case Created -> {
        firstPlayerId = playerId;
        activePlayerId = firstPlayerId;
        field = firstPlayerField;
        state = GameState.WaitingSecondPlayer;
        planMatchmakingTimeoutTask();
      }
      case WaitingSecondPlayer -> {
        if (playerId.equals(firstPlayerId))
          throw new IllegalArgumentException("Игрок не может вступить в бой сам с собой");

        field = secondPlayerField;
        secondPlayerId = playerId;

        state = GameState.Started;

        startedAt = LocalDateTime.now();
        log.info("Начат бой между игроками с id {} и {}", firstPlayerId, secondPlayerId);

        planFireTimeoutTask();
        // Планируем задачку завершения сессии по таймауту хода

      }
      default -> throw new IllegalArgumentException("В это состоянии игры невозможно добавлять игроков");
    }

    for (var boat : cords)
      field.placeBoat(boat);

    if (field.getAliveShips() != max_score)
      throw new IllegalStateException("Недопустимая расстановка кораблей. Должно быть %d палуб. А на деле: %d"
          .formatted(max_score, field.getAliveShips()));

    stateUpdated();
  }

  public int makeShot(long playerId, int x, int y) {

    if (state != GameState.Started)
      throw new IllegalStateException("Нельзя делать выстрелы в этом состояние");

    if (!(playerId == firstPlayerId || playerId == secondPlayerId))
      throw new IllegalArgumentException("Игрок не состоит в этой игре");

    if (playerId != activePlayerId)
      throw new IllegalStateException("Сейчас очередь опонента");

    planFireTimeoutTask();

    log.trace("Игрок {} делает выстрел по координатам {}:{}", playerId, x, y);

    log.info("FPF: {}, SPF: {}", firstPlayerField, secondPlayerField);

    int aliveShips;
    if (playerId == firstPlayerId) {
      if (!secondPlayerField.makeShot(x, y))
        activePlayerId = secondPlayerId;
      aliveShips = secondPlayerField.getAliveShips();
    } else {
      if (!firstPlayerField.makeShot(x, y))
        activePlayerId = firstPlayerId;
      aliveShips = firstPlayerField.getAliveShips();
    }

    log.info("Осталось {} живых палуб", aliveShips);

    if (aliveShips == 0) {
      state = GameState.Ended;
      failTask.dispose();
    }
    stateUpdated();

    return aliveShips;
  }

  /**
   * Завершить игровую сессию с ошибкой
   */
  public void fail(String reason) {
    log.warn("Завершение игровой сессии {} по ошибке {}", sessionId, reason);
    state = GameState.Failed;
    stateUpdated();
  }

  @Override
  public int hashCode() {
    return sessionId.hashCode();
  }

  public GameField getField(long playerId) {
    if (state != GameState.Started)
      throw new IllegalStateException("Нельзя получить поле до начала игры");

    if (playerId == firstPlayerId)
      return firstPlayerField;
    else if (playerId == secondPlayerId)
      return secondPlayerField;
    else
      throw new IllegalArgumentException("Игрок не состоит в этой игре");

  }

  private void stateUpdated() {
    version++;
    waitService.signal(sessionId.toString(), this);
  }

}
