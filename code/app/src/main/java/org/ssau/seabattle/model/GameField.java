package org.ssau.seabattle.model;

import org.openapitools.model.BoatCord;
import org.openapitools.model.GameSettings;

/**
 * <p>
 * Игровое поле
 * </p>
 * 
 * Реализует следующий функционал:
 * <ul>
 * <li>Хранение состояния поля
 * <li>Отображение поля для владельца и противника
 * <li>Логика расстановки кораблей
 * <li>Логика выстрела
 * <li>Посчет живых кораблей на поле
 * </ul>
 */
public class GameField {

  private final OwnerCageState[][] ownerMap;
  private final OpponentCageState[][] opponentMap;
  private final int width, height;

  public GameField(GameSettings gameSettings) {
    this.width = gameSettings.getGameMapSettings().getWidth();
    this.height = gameSettings.getGameMapSettings().getHeight();
    ownerMap = new OwnerCageState[width][height];
    opponentMap = new OpponentCageState[width][height];
  }

  /**
   * Сделать выстрел по координатам(отсчет от нуля)
   * 
   * @param x
   * @param y
   * @return
   */
  public OwnerCageState makeShot(int x, int y) {
    OwnerCageState prevState = ownerMap[x][y];

    switch (prevState) {
      case Void -> {
        ownerMap[x][y] = OwnerCageState.ShotIntoVoid;
        opponentMap[x][y] = OpponentCageState.CheckedEmpty;
      }
      case Ship -> {
        ownerMap[x][y] = OwnerCageState.ShotIntoShip;
        opponentMap[x][y] = OpponentCageState.BlowedShip;
        markEmptyCages(x, y);
      }
      case ShotIntoShip, ShotIntoVoid -> {
      }
    }

    return ownerMap[x][y];
  }

  private void markEmptyCages(int x, int y) {
    // TODO
  }

  public void placeShip(BoatCord cords) {

    // TODO
    throw new UnsupportedOperationException();
  }

  enum OwnerCageState {
    Void, Ship, ShotIntoVoid, ShotIntoShip
  }

  enum OpponentCageState {
    Unchecked, CheckedEmpty, UncheckedEmpty, BlowedShip
  }

  public int getHeight() {
    return height;
  }

  public int getWidth() {
    return width;
  }
}
