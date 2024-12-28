package org.ssau.sandbox.domain.game;

import org.openapitools.model.UserProfileDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.ssau.sandbox.domain.user.AppUser;
import org.ssau.sandbox.repository.AvatarRepository;

import reactor.core.publisher.Mono;

/**
 * DtoFactory
 */
@Component
public class DtoFactory {

  @Autowired
  private AvatarRepository avatarRepository;

  public Mono<UserProfileDto> toDto(AppUser user) {
    var dto = new UserProfileDto();
    dto.setLogin(user.getUsername());
    dto.setUserId(user.getId());
    dto.setAvatarId(user.getAvatarId());

    return avatarRepository.findById(user.getAvatarId()).map(avatar -> {
      dto.setPictureUrl(avatar.getPicture_url());
      return dto;
    });
  };

}
