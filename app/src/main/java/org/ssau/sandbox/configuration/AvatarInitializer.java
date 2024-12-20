package org.ssau.sandbox.configuration;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.ssau.sandbox.domain.user.Avatar;
import org.ssau.sandbox.repository.AvatarRepository;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

/**
 * DatabaseInitializer
 */
@Slf4j
@Component
@Order(2)
public class AvatarInitializer implements ApplicationRunner {

  @Autowired
  AvatarRepository repository;

  @Override
  public void run(ApplicationArguments args) throws Exception {
    var count = repository.findAll().count().block();
    if (count > 1)
      return;

    var defaultAvatars = List.of(
        "https://avatars.mds.yandex.net/i?id=7dac36b794faa3da0cd10c04b1bd7792_l-4115767-images-thumbs&n=13",
        "https://steamuserimages-a.akamaihd.net/ugc/2122942598541842255/3601D3916B0D5F1F6BD45940D9927E353EBEAAE4/?imw=5000&imh=5000&ima=fit&impolicy=Letterbox&imcolor=%23000000&letterbox=false",
        "https://pic.rutubelist.ru/user/0e/c0/0ec0b343e1bb1c7fced16f74a2459f1c.jpg");

    Flux.fromIterable(defaultAvatars)
        .map(url -> new Avatar(url))
        .flatMap(a -> repository.save(a))
        .blockLast();

    log.info("Миграция успешно выполнена");
  }

}
