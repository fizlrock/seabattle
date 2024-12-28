package org.ssau.sandbox.domain.game;

import org.springframework.stereotype.Component;
import org.ssau.sandbox.domain.game.GameSession.GameState;

/**
 * EntityFactory
 */
@Component
public class EntityFactory {

  public GameSessionRecord toEntity(GameSession session) {

    GameSessionRecord gsr = new GameSessionRecord();

    if (session.getState() != GameState.Ended)
      throw new IllegalArgumentException("Можно сериализовать только завершенную сессию");

    gsr.setSessionId(session.getSessionId());
    gsr.setFirstPlayerId(session.getFirstPlayerId());
    gsr.setSecondPlayerId(session.getSecondPlayerId());
    gsr.setWinnerPlayerId(session.getActivePlayerId());
    gsr.setStartedAt(session.getStartedAt());
    gsr.setEndedAt(session.getEndedAt());

    gsr.setTotalShots(session.getTotalShots());
    gsr.setFirstPlayerShotsInTarget(session.getFirstPlayerShotsInTarget());
    gsr.setSecondPlayerShotsInTarget(session.getSecondPlayerShotsInTarget());

    return gsr;

  }

}
