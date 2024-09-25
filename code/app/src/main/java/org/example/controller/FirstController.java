package org.example.controller;

import java.time.Duration;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import org.openapitools.api.*;
import org.openapitools.model.BoatCord;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class FirstController implements BoatPlacementApi {


	@Override
	public Mono<ResponseEntity<Flux<BoatCord>>> getGeneratedBoatSpec(@NotNull @Valid Long strategyId,
			ServerWebExchange exchange) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'getGeneratedBoatSpec'");
	}

}
