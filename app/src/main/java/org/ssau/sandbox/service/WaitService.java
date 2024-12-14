package org.ssau.sandbox.service;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

@Service
@Slf4j
public class WaitService {

  private final ConcurrentHashMap<String, Sinks.One<Object>> sinks = new ConcurrentHashMap<>();

  public Mono<Object> waitForSignal(String id) {
    log.info("sinks: {}", sinks);
    Sinks.One<Object> sink = Sinks.one();
    sinks.put(id, sink);
    return sink.asMono()
        // .doFinally(signalType -> sinks.remove(id))
        .timeout(Duration.ofSeconds(15)); // Удаляем поток после завершения
  }

  public Mono<Void> signal(String id, Object response) {
    log.info("sinks: {}", sinks);
    Sinks.One<Object> sink = sinks.get(id);
    if (sink != null) {
      sink.tryEmitValue(response); // Завершаем ожидание
    }
    return Mono.empty();
  }

}
