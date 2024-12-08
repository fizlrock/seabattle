package org.ssau.sandbox.service;

import java.util.Collection;

import org.springframework.stereotype.Service;
import org.ssau.sandbox.domain.game.BoatCord;
import org.ssau.sandbox.domain.user.AppUser;

/**
 * Контроль за очередностью хода
 * GameService
 */
@Service
public class GameService {

  // TODO
  /**
   * Начать новую игру.
   * Пользователь должен быть авторизован.
   * 
   * @param username - имя пользователя
   * @param cords    - список координат кораблей
   */
  public void startGame(AppUser user, Collection<BoatCord> cords) {
    // TODO Проверить пулл матч мейкнинга

  }

}
