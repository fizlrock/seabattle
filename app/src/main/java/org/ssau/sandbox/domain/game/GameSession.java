package org.ssau.sandbox.domain.game;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.ssau.sandbox.domain.game.field.GameField;

import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 * Содержит логику изменения состояния игры и проверку допустимости действий
 * Сессия игры в морской бой
 * SeabattleGame
 */
@Slf4j
@ToString
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
  private Long activePlayerId;

  private final LocalDateTime createdAt = LocalDateTime.now();
  private LocalDateTime startedAt;
  private LocalDateTime endedAt;

  public Long getFirstPlayerId() {
    return firstPlayerId;
  }

  public Long getSecondPlayerId() {
    return secondPlayerId;
  }

  public Long getActivePlayerId() {
    return activePlayerId;
  }

  private final GameSettings settings;

  private GameState state;

  public GameSession(GameSettings settings) {
    sessionId = UUID.randomUUID();
    log.debug("Создание новой игровой сессии.ID: {}", sessionId);
    this.settings = settings;

    firstPlayerField = new GameField(settings);
    secondPlayerField = new GameField(settings);
    state = GameState.Created;
  }

  public GameSession(GameSettings settings, long playerId, Collection<BoatCord> cords) {
    this(settings);
    log.debug("GameSession: {}. Первый игрок: {}", sessionId, playerId);

    // TODO а может ли пользователь участвовать в нескольких играх?
    // Думаю это стоить проверять в сервисе

    // TODO стоит расставлять корабли более эффективно
    for (var boat : cords)
      firstPlayerField.placeBoat(boat);

    firstPlayerId = playerId;
    activePlayerId = playerId;

    state = GameState.WaitingSecondPlayer;

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

    switch (state) {
      case Started, Created -> {
        firstPlayerId = playerId;
        for (var boat : cords)
          firstPlayerField.placeBoat(boat);
        state = GameState.WaitingSecondPlayer;
        version++;
      }
      case WaitingSecondPlayer -> {

        if (playerId.equals(firstPlayerId)) {
          throw new IllegalArgumentException("Игрок не может вступить в бой сам с собой");
        }
        secondPlayerId = playerId;
        for (var boat : cords)
          secondPlayerField.placeBoat(boat);
        state = GameState.Started;
        startedAt = LocalDateTime.now();
        version++;
      }
      default -> throw new IllegalArgumentException("В это состоянии игры невозможно добавлять игроков");
    }
  }

  public int makeShot(long playerId, int x, int y) {

    if (state != GameState.Started)
      throw new IllegalStateException("Нельзя делать выстрелы в этом состояние");

    if (playerId != firstPlayerId || playerId != secondPlayerId)
      throw new IllegalArgumentException("Игрок не состоит в этой игре");

    if (playerId != activePlayerId)
      throw new IllegalStateException("Сейчас очередь опонента");

    log.trace("Игрок {} делает выстрел по координатам {}:{}", playerId, x, y);

    version++;

    if (playerId == firstPlayerId)
      return secondPlayerField.makeShot(x, y);
    else
      return firstPlayerField.makeShot(x, y);

  }

  @Override
  public int hashCode() {
    return sessionId.hashCode();
  }

}
