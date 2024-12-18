package org.ssau.sandbox.api;

import java.util.function.Function;

import org.openapitools.api.GameApi;
import org.openapitools.model.BoatCordDto;
import org.openapitools.model.BoatTypeDto;
import org.openapitools.model.GameMapSettingsDto;
import org.openapitools.model.GameSettingsDto;
import org.openapitools.model.GameStateDto;
import org.openapitools.model.ShotDto;
import org.springframework.security.oauth2.core.DefaultOAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import org.ssau.sandbox.domain.game.BoatCord;
import org.ssau.sandbox.domain.game.BoatType;
import org.ssau.sandbox.domain.game.GameMapSettings;
import org.ssau.sandbox.domain.game.GameSettings;
import org.ssau.sandbox.service.GameService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@Slf4j
public class GameController implements GameApi {

  // TODO от этого нужно избавится
  private Mono<DefaultOAuth2AuthenticatedPrincipal> getPrincipal(ServerWebExchange exchange) {

    return exchange.getPrincipal()
        .cast(BearerTokenAuthentication.class)
        .map(BearerTokenAuthentication::getPrincipal)
        .cast(DefaultOAuth2AuthenticatedPrincipal.class);
  }

  private final GameService service;

  private static Function<BoatType, BoatTypeDto> boatTypeToDto = boat -> (new BoatTypeDto())
      .count(boat.count())
      .size(boat.size());

  private static Function<GameMapSettings, GameMapSettingsDto> mapSettingsToDto = mapSettings -> new GameMapSettingsDto(
      mapSettings.width(), mapSettings.height());

  private static Function<GameSettings, GameSettingsDto> gameSettingsToDto = settings -> {
    var boatTypes = settings.boatTypes().stream()
        .map(boatTypeToDto)
        .toList();

    var mapSettingsDto = mapSettingsToDto.apply(settings.mapSettings());
    return new GameSettingsDto(boatTypes, mapSettingsDto);// TODO
  };

  @Override
  public Mono<GameSettingsDto> getGameSettings(ServerWebExchange exchange) {
    return Mono.just(service.gameSettings)
        .map(x -> gameSettingsToDto.apply(x));
  }

  @Override
  public Mono<GameStateDto> getUpdatedGameState(@NotNull @Min(0) @Valid Long currentStateNumber,
      ServerWebExchange exchange) {

    var principal = getPrincipal(exchange);
    var user_id = principal.map(
        x -> x.getAttribute("user_id"))
        .cast(Integer.class)
        .map(x -> x.longValue());

    return user_id.flatMap(id -> service.getUpdatedGameState(id,
        currentStateNumber));
  }

  @Override
  public Mono<GameStateDto> makeShot(@Valid Mono<ShotDto> shotDto, ServerWebExchange exchange) {
    var principal = getPrincipal(exchange);

    var user_id = principal.map(
        x -> x.getAttribute("user_id"))
        .cast(Integer.class)
        .map(x -> x.longValue());

    return Mono.zip(user_id, shotDto)
        .map(x -> service.makeShot(x.getT1(), x.getT2().getX(), x.getT2().getY()));
  }

  @Override
  public Mono<GameStateDto> startNewGame(@Valid Flux<BoatCordDto> boatCordDto, ServerWebExchange exchange) {

    var principal = getPrincipal(exchange);

    var user_id = principal.map(
        x -> x.getAttribute("user_id"))
        .cast(Integer.class)
        .map(x -> x.longValue());

    var boats = boatCordDto
        .map(GameController::fromDto)
        .collectList();

    return Mono.zip(user_id, boats)
        .flatMap(x -> service.startGame(x.getT1(), x.getT2()));

  }

  private static BoatCord fromDto(BoatCordDto dto) {
    return new BoatCord(dto.getXs(), dto.getYs(), dto.getXe(), dto.getYe());

  }

}
