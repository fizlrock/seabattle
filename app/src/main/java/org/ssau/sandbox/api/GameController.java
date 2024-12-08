package org.ssau.sandbox.api;

import org.openapitools.api.GameApi;
import org.openapitools.model.BoatCordDto;
import org.openapitools.model.GameSettingsDto;
import org.openapitools.model.GameStateDto;
import org.openapitools.model.ShotDto;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class GameController implements GameApi {
  @Override
  public Mono<GameSettingsDto> getGameSettings(ServerWebExchange exchange) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getGameSettings'");
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
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'makeShot'");

  }

}
