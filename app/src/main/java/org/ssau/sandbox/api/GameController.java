package org.ssau.sandbox.api;

import java.util.function.Function;

import org.openapitools.api.GameApi;
import org.openapitools.model.BoatCordDto;
import org.openapitools.model.BoatTypeDto;
import org.openapitools.model.GameMapSettingsDto;
import org.openapitools.model.GameSettingsDto;
import org.openapitools.model.GameStateDto;
import org.openapitools.model.ShotDto;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class GameController implements GameApi {

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
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getUpdatedGameState'");
  }

  @Override
  public Mono<GameStateDto> makeShot(@Valid Mono<ShotDto> shotDto, ServerWebExchange exchange) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'makeShot'");
  }

  @Override
  public Mono<GameStateDto> startNewGame(@Valid Flux<BoatCordDto> boatCordDto, ServerWebExchange exchange) {

    // TODO take user id

    return boatCordDto
        .map(GameController::fromDto)
        .collectList()
        .flatMap(boats -> service.startGame(0l, boats));

  }

  private static BoatCord fromDto(BoatCordDto dto) {
    return new BoatCord(dto.getXs(), dto.getYs(), dto.getXe(), dto.getYe());

  }

}
