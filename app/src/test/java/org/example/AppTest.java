/*
 * This source file was generated by the Gradle 'init' task
 */
package org.example;

import org.junit.jupiter.api.Test;
import org.ssau.sandbox.domain.game.BoatCord;
import org.ssau.sandbox.domain.game.BoatType;
import org.ssau.sandbox.domain.game.GameMapSettings;
import org.ssau.sandbox.domain.game.GameSettings;
import org.ssau.sandbox.domain.game.field.GameField;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;

class AppTest {
  @Test
  void appHasAGreeting() {
    assertEquals(2, 1 + 1);
  }

  static final GameSettings gameSettings = new GameSettings(new GameMapSettings(10, 10), List.of(
      new BoatType(4, 1),
      new BoatType(3, 2),
      new BoatType(2, 3),
      new BoatType(1, 4)));

  @Test
  void gameFieldPlaceBoatTest() {

    var field = new GameField(gameSettings);

    List<BoatCord> placement = List.of(
        new BoatCord(0, 0, 3, 0),
        new BoatCord(1, 2, 1, 4));

    field.placeBoat(placement.get(0));
    field.placeBoat(placement.get(1));

    assertEquals(7, field.getAliveShips(), "Число палуб не совпадает");

    var stil_boats = field.getStilBoats();
    var stil_boats_exp = Map.of(
        4, 0,
        3, 1,
        2, 3,
        1, 4);

    assertEquals(stil_boats_exp, stil_boats);

    var own_map = field.getOwnerMap();
    var exp_own_map = new int[][] {
        { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
        { 1, 0, 1, 1, 1, 0, 0, 0, 0, 0 },
        { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
        { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
        { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
        { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
        { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
        { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
        { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
        { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
    };
    assertArrayEquals(exp_own_map, own_map);
    var opp_map = field.getOpponentMap();
    var exp_opp_map = new int[][] {
        { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
        { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
        { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
        { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
        { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
        { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
        { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
        { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
        { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
        { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
    };
    assertArrayEquals(exp_opp_map, opp_map);
  }

  @Test
  void makeShotTest() {

    var field = new GameField(gameSettings);

    List<BoatCord> placement = List.of(
        new BoatCord(0, 0, 3, 0),
        new BoatCord(1, 2, 1, 4));

    field.placeBoat(placement.get(0));
    field.placeBoat(placement.get(1));

    assertEquals(7, field.getAliveShips());

    for (int i = 0; i < 10; i++)
      field.makeShot(i, i);
    assertEquals(6, field.getAliveShips());

    var own_map = field.getOwnerMap();
    var exp_own_map = new int[][] {
        { 3, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
        { 1, 2, 1, 1, 1, 0, 0, 0, 0, 0 },
        { 1, 0, 2, 0, 0, 0, 0, 0, 0, 0 },
        { 1, 0, 0, 2, 0, 0, 0, 0, 0, 0 },
        { 0, 0, 0, 0, 2, 0, 0, 0, 0, 0 },
        { 0, 0, 0, 0, 0, 2, 0, 0, 0, 0 },
        { 0, 0, 0, 0, 0, 0, 2, 0, 0, 0 },
        { 0, 0, 0, 0, 0, 0, 0, 2, 0, 0 },
        { 0, 0, 0, 0, 0, 0, 0, 0, 2, 0 },
        { 0, 0, 0, 0, 0, 0, 0, 0, 0, 2 },
    };
    assertArrayEquals(exp_own_map, own_map);

    var opp_map = field.getOpponentMap();
    var exp_opp_map = new int[][] {
        { 3, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
        { 0, 1, 0, 0, 0, 0, 0, 0, 0, 0 },
        { 0, 0, 1, 0, 0, 0, 0, 0, 0, 0 },
        { 0, 0, 0, 1, 0, 0, 0, 0, 0, 0 },
        { 0, 0, 0, 0, 1, 0, 0, 0, 0, 0 },
        { 0, 0, 0, 0, 0, 1, 0, 0, 0, 0 },
        { 0, 0, 0, 0, 0, 0, 1, 0, 0, 0 },
        { 0, 0, 0, 0, 0, 0, 0, 1, 0, 0 },
        { 0, 0, 0, 0, 0, 0, 0, 0, 1, 0 },
        { 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
    };
    assertArrayEquals(exp_opp_map, opp_map);

  }

}
