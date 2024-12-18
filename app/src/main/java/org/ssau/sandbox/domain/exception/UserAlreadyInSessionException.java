package org.ssau.sandbox.domain.exception;

/**
 * UserAlreadyInSessionException
 */
public class UserAlreadyInSessionException extends SeabattleException {

  public UserAlreadyInSessionException() {
    super("Пользователь уже состоит в игровой сессии.");

  }

}
