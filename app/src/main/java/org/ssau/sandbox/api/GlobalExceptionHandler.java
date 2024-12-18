package org.ssau.sandbox.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.ssau.sandbox.domain.exception.SeabattleException;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * GlobalExceptionHandler
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  @ExceptionHandler(Exception.class)
  public Mono<ResponseEntity<String>> handleGeneralException(Exception ex) {

    log.error("ОшибОчка: {}", ex);
    return Mono.just(ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body("Внутренняя ошибка сервера: " + ex.getMessage()));
  }

  @ExceptionHandler(WebExchangeBindException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public String handleWebExchangeBindException(WebExchangeBindException ex) {
    // Извлекаем первое поле, которое вызвало ошибку
    FieldError fieldError = ex.getFieldErrors().get(0);
    // Возвращаем сообщение с указанием поля
    return "Не допустимое значение поля: " + fieldError.getField();
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public Mono<ResponseEntity<String>> handle(IllegalArgumentException ex) {
    // log.debug("Ошибка: {}", ex);
    return Mono.just(ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(ex.getMessage()));
  }

  @ExceptionHandler(SeabattleException.class)
  @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
  public String handle(SeabattleException ex) {
    return ex.getMessage();
  }


  
}
