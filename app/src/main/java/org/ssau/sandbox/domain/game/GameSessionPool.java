package org.ssau.sandbox.domain.game;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Component;
import org.ssau.sandbox.domain.game.GameSession.GameState;

/**
 * Структура хранит активные игры
 * GameSessionPool
 */
@Component
public class GameSessionPool {

  public final Set<GameSession> sessions = new HashSet<>();

  private GameSession createNewSession() {
    var session = new GameSession();
    sessions.add(session);
    return session;
  }

  public GameSession findSession() {
    var session = sessions.stream()
        .filter(s -> s.getState() == GameState.WaitingSecondPlayer)
        .findAny();

    return session.orElseGet(this::createNewSession);
  }

}
