/*
 * This source file was generated by the Gradle 'init' task
 */
package org.ssau.seabattle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.ssau.seabattle.repository.UserRepository;
import org.ssau.seabattle.userModel.UserEntity;

@SpringBootApplication
public class App {
  public static void main(String[] args) {
    var context = SpringApplication.run(App.class);

    // var repo = context.getBean(UserRepository.class);


    // repo.findByUsername("fizlrock");
  }

}
