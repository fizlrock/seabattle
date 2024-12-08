package org.ssau.sandbox.domain.game;

import java.util.Collection;
import java.util.UUID;

import org.ssau.sandbox.domain.game.field.GameField;
import org.ssau.sandbox.domain.user.AppUser;

import lombok.extern.slf4j.Slf4j;

/**
 * Содержит логику изменения состояния игры и проверку допустимости действий
 * Сессия игры в морской бой
 * SeabattleGame
 */
@Slf4j
public class GameSession {

  public static enum GameState {
    WaitingSecondPlayer, Ended, Failed, Started
  }


  private final UUID sessionId;

  private long version = 0;

  private final GameField firstPlayerField;

  private final GameField secondPlayerField;

  private String firstPlayer;

  private String secondPlayer;
  private String activePlayer;

  private GameSettings settings;

  private GameState state;

  public GameSession(String user, Collection<BoatCord> cords) {

    if (user == null)
      throw new IllegalArgumentException("User can't be null");
    // TODO а может ли пользователь участвовать в нескольких играх?
    // Думаю это стоить проверять в сервисе

    sessionId = UUID.randomUUID();
    log.debug("Создание новой игровой сессии.ID: {} Инициатор: {}", sessionId, user);

    firstPlayerField = new GameField(null);
    secondPlayerField = new GameField(null);

    // TODO стоит расставлять корабли более эффективно
    for (var boat : cords)
      firstPlayerField.placeBoat(boat);

    firstPlayer = user;
    activePlayer = user;

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

  public String getFirstPlayer() {
    return firstPlayer;
  }

  public String getSecondPlayer() {
    return secondPlayer;
  }

  public String getActivePlayer() {
    return activePlayer;
  }

  public GameSettings getSettings() {
    return settings;
  }

  public GameState getState() {
    return state;
  }

  public void setOpponent(String user, Collection<BoatCord> cords) {
    if (user == null)
      throw new IllegalArgumentException("User can't be null");
    // TODO а может ли пользователь участвовать в нескольких играх?
    // Думаю это стоить проверять в сервисе
    secondPlayer = user;
    for (var boat : cords)
      secondPlayerField.placeBoat(boat);

    version++;

    state = GameState.Started;
  }

  public int makeShot(AppUser user, int x, int y) {

    if (state != GameState.Started)
      throw new IllegalStateException("Нельзя делать выстрелы в этом состояние");

    if (!(user.equals(firstPlayer) || user.equals(secondPlayer)))
      throw new IllegalArgumentException("Игрок не состоит в этой игре");

    if (!user.equals(activePlayer))
      throw new IllegalStateException("Сейчас очередь опонента");

    log.trace("Игрок {} делает выстрел по координатам {}:{}", user.getUsername(), x, y);

    version++;

    if (user.equals(firstPlayer))
      return secondPlayerField.makeShot(x, y);
    else
      return firstPlayerField.makeShot(x, y);

  }

}
