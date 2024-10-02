package org.ssau.seabattle.model;

/**
 * GameMapSerializer
 */
public interface GameMapSerializer {

  public String serializeOpponentMap(GameField field);
  public String serializeOwnerMap(GameField field);


}
