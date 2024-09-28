package org.ssau.seabattle.model;

import org.ssau.seabattle.model.GameField.OpponentCageState;
import org.ssau.seabattle.model.GameField.OwnerCageState;

/**
 * GameMapSerializer
 */
public interface GameMapSerializer {

  public String serialize(OpponentCageState[] map);

  public String serialize(OwnerCageState[] map);

}
