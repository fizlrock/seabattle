package org.ssau.sandbox.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import reactor.core.publisher.Mono;

/**
 * ExceptionHandler
 */
@RestControllerAdvice
public class ApiExceptionHandler {

  @ExceptionHandler(BadCredentialsException.class)
  public Mono<ResponseEntity<String>> example(BadCredentialsException exception) {
    return Mono.just(ResponseEntity.badRequest().body(exception.toString()));
  }
}
