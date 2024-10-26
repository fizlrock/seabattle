package org.ssau.sandbox.auth.exception;

/**
 * TokenExpiredException
 */
public class TokenExpiredException extends RuntimeException {
  public TokenExpiredException() {
    super("Срок действия токена истек");
  }

}
