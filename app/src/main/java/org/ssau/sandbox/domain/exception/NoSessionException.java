package org.ssau.sandbox.domain.exception;

/**
 * NoSessionException
 */
public class NoSessionException extends SeabattleException {

  public NoSessionException() {
    super("Игрок не состоит в игровой сессии");
  }

}
