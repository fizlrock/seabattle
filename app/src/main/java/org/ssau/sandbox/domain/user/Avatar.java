package org.ssau.sandbox.domain.user;

import org.springframework.data.annotation.Id;

import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * Avatar
 */
@Data
@RequiredArgsConstructor
public class Avatar {
  @Id
  private Long id;

  private final String picture_url;

}
