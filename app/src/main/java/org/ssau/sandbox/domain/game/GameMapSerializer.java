package org.ssau.sandbox.domain.game;

import org.ssau.sandbox.domain.game.field.GameField;

/**
 * GameMapSerializer
 */
public interface GameMapSerializer {

  public String serializeOpponentMap(GameField field);
  public String serializeOwnerMap(GameField field);


}
