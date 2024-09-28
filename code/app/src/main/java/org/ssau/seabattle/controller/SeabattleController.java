package org.ssau.seabattle.controller;

import java.util.List;

import org.openapitools.api.SeabattleApi;
import org.openapitools.model.AuthData;
import org.openapitools.model.BoatCord;
import org.openapitools.model.GameSettings;
import org.openapitools.model.GameState;
import org.openapitools.model.PlacementStrategy;
import org.openapitools.model.Shot;
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
	public Mono<ResponseEntity<GameSettings>> getGameSettings(ServerWebExchange exchange) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'getGameSettings'");
	}

	@Override
	public Mono<ResponseEntity<Flux<BoatCord>>> getGeneratedBoatSpec(@NotNull @Valid Long strategyId,
			ServerWebExchange exchange) {
		return Mono.just(
				ResponseEntity.ok(Flux.fromIterable(List.of(
						new BoatCord().xs(0).ys(0).xe(4).ye(0),
						new BoatCord().xs(0).ys(0).xe(3).ye(0),
						new BoatCord().xs(0).ys(0).xe(2).ye(0),
						new BoatCord().xs(0).ys(0).xe(1).ye(0)))));
	}

	@Override
	public Mono<ResponseEntity<PlacementStrategy>> getPlacementStrategies(ServerWebExchange exchange) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'getPlacementStrategies'");
	}

	@Override
	public Mono<ResponseEntity<GameState>> getUpdatedGameState(@NotNull @Min(0) @Valid Long currentStateNumber,
			ServerWebExchange exchange) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'getUpdatedGameState'");
	}

	@Override
	public Mono<ResponseEntity<GameState>> seabattleGameShotPost(@Valid Mono<Shot> shot, ServerWebExchange exchange) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'seabattleGameShotPost'");
	}

	@Override
	public Mono<ResponseEntity<GameState>> startNewGame(@Valid Flux<BoatCord> boatCord, ServerWebExchange exchange) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'startNewGame'");
	}

	@Override
	public Mono<ResponseEntity<String>> getToken(@Valid Mono<AuthData> authData, ServerWebExchange exchange) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'getToken'");
	}
}
