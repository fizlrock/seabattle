package org.ssau.seabattle.controller;

import org.openapitools.api.SeabattleApi;
import org.openapitools.model.AuthDataDto;
import org.openapitools.model.BoatCordDto;
import org.openapitools.model.GameSettingsDto;
import org.openapitools.model.GameStateDto;
import org.openapitools.model.PlacementStrategyDto;
import org.openapitools.model.ShotDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class SeabattleController implements SeabattleApi {

	@Override
	public Mono<ResponseEntity<GameSettingsDto>> getGameSettings(ServerWebExchange exchange) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'getGameSettings'");
	}

	@Override
	public Mono<ResponseEntity<Flux<BoatCordDto>>> getGeneratedBoatSpec(@NotNull @Valid Long strategyId,
			ServerWebExchange exchange) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'getGeneratedBoatSpec'");
	}

	@Override
	public Mono<ResponseEntity<PlacementStrategyDto>> getPlacementStrategies(ServerWebExchange exchange) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'getPlacementStrategies'");
	}

	@Override
	public Mono<ResponseEntity<String>> getToken(@Valid Mono<AuthDataDto> authDataDto, ServerWebExchange exchange) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'getToken'");
	}

	@Override
	public Mono<ResponseEntity<GameStateDto>> getUpdatedGameState(@NotNull @Min(0) @Valid Long currentStateNumber,
			ServerWebExchange exchange) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'getUpdatedGameState'");
	}

	@Override
	public Mono<ResponseEntity<GameStateDto>> makeShot(@Valid Mono<ShotDto> shotDto, ServerWebExchange exchange) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'makeShot'");
	}

	@Override
	public Mono<ResponseEntity<GameStateDto>> startNewGame(@Valid Flux<BoatCordDto> boatCordDto,
			ServerWebExchange exchange) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'startNewGame'");
	}
}
