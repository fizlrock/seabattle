package org.ssau.sandbox.service;

import java.time.Duration;
import java.util.Collection;
import java.util.List;

import org.openapitools.model.GameStateDto;
import org.openapitools.model.GameStateDto.StatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ssau.sandbox.domain.game.BoatCord;
import org.ssau.sandbox.domain.game.BoatType;
import org.ssau.sandbox.domain.game.GameMapSerializer;
import org.ssau.sandbox.domain.game.GameMapSettings;
import org.ssau.sandbox.domain.game.GameSession;
import org.ssau.sandbox.domain.game.GameSession.GameState;
import org.ssau.sandbox.domain.game.GameSessionPool;
import org.ssau.sandbox.domain.game.GameSettings;
import org.ssau.sandbox.domain.game.field.GameField;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * Контроль за очередностью хода
 * GameService
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class GameService {

  private final GameSessionPool sessionPool;
  private final GameMapSerializer mapSerializer;

  public final GameSettings gameSettings = new GameSettings(
      new GameMapSettings(10, 10),
      List.of(
          new BoatType(1, 4),
          new BoatType(2, 3),
          new BoatType(3, 2),
          new BoatType(4, 1))

  );

  /**
   * Начать новую игру.
   * Пользователь должен быть авторизован.
   * 
   * @param username - имя пользователя
   * @param cords    - список координат кораблей
   */
  public Mono<GameStateDto> startGame(Long userId, Collection<BoatCord> cords) {

    log.info("Пользователь с id {} хочет вступить в поединок", userId);

    var session = sessionPool.findSession();
    session.addPlayer(userId, cords);

    log.info("Start game with session: {}", session);

    return Mono.just(session).map(s -> toDto(s, userId));

  }

  @Autowired
  WaitService waitService;

  public Mono<GameStateDto> getUpdatedGameState(Long userId, Long currentStateNumber) {

    log.info("Запрос обновленного состояния игры. userId: {} state: {}", userId, currentStateNumber);
    var session = sessionPool.findUserSession(userId)
        .orElseThrow(() -> new IllegalStateException("Игрок не состоит в игре"));

    if (session.getVersion() < currentStateNumber)
      return Mono.just(session).map(s -> toDto(s, userId));
    else {
      return waitService.waitForSignal(session.getSessionId().toString())
          .cast(GameSession.class)
          .timeout(Duration.ofSeconds(10), Mono.just(session))
          .map(s -> toDto(s, userId));

    }

  }

  public GameStateDto toDto(GameSession session, Long playerId) {
    var dto = new GameStateDto();

    StatusEnum dto_status = switch (session.getState()) {
      case Created -> null; // TODO
      case Ended -> StatusEnum.ENDED;
      case Failed -> StatusEnum.FAILED;
      case Started -> StatusEnum.STARTED;
      case WaitingSecondPlayer -> StatusEnum.WAITING_SECOND_PLAYER;
    };

    dto.setChangeCount(session.getVersion());
    dto.setStatus(dto_status);
    dto.youShoting(false);

    if (session.getState() == GameState.Started) {
      dto.youShoting(session.getActivePlayerId() == playerId);

      GameField yourField, oppoField;

      if (session.getFirstPlayerId() == playerId) {
        yourField = session.getFirstPlayerField();
        oppoField = session.getSecondPlayerField();
        dto.setOponentName(session.getSecondPlayerId().toString());
      } else if (session.getSecondPlayerId() == playerId) {
        yourField = session.getSecondPlayerField();
        oppoField = session.getFirstPlayerField();
        dto.setOponentName(session.getFirstPlayerId().toString());
      } else
        throw new IllegalArgumentException("Пользователя не состоит в игре");

      dto.setYourField(mapSerializer.serializeOwnerMap(yourField));
      dto.setOponentField(mapSerializer.serializeOpponentMap(oppoField));
    }

    return dto;
  }

}
