
package org.ssau.sandbox;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration;

import lombok.extern.slf4j.Slf4j;

// @SpringBootApplication()

@SpringBootApplication(exclude = { ReactiveSecurityAutoConfiguration.class })
@Slf4j
public class App {
  public static void main(String[] args) {
    var context = SpringApplication.run(App.class, args);
  }
}
