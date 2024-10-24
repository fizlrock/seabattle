package org.ssau.sandbox.controller;

import java.net.HttpCookie;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import org.ssau.sandbox.service.TokenService;

import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TokenApiController {

  private final TokenService securityService;

  @CrossOrigin(origins = "*")
  @GetMapping("/api/token")
  public Mono<String> getToken(
      ServerWebExchange ex) {

    log.info("auth {}", ex.getPrincipal());

    return ex.getPrincipal()
        .map(securityService::getToken)
        .switchIfEmpty(Mono.error(
            new BadCredentialsException(
                "You need to use base-auth to get token")));
  }

  // POST /user
  // GET /token - получить токен авторизации
  // GET /user/{userId} - получить информацию об игроке
  // GET /user - получить список всех игроков

  @GetMapping("/share")
  public Mono<ResponseEntity<String>> auth(ServerWebExchange ex) throws JsonProcessingException {

    var req = ex.getRequest();

    String reqReport = Stream.<Supplier<?>>of(
        req::getBody,
        req::getCookies,
        req::getHeaders,
        req::getLocalAddress,
        req::getRemoteAddress)
        .map(Supplier::get)
        .map(Object::toString)
        .collect(Collectors.joining("\n"));
    log.info(reqReport);

    // List<Supplier<?>> supps = List.of(
    // req::getBody,
    // req::getCookies,
    // req::getHeaders,
    // req::getLocalAddress,
    // req::getRemoteAddress
    // );

    return Mono.just(ResponseEntity.ok("Hey it's not private method\b"));
  }

  @GetMapping("/private")
  public Mono<ResponseEntity<String>> priv() {
    return Mono.just(ResponseEntity.ok("Hey it's not private method\b"));
  }

}
