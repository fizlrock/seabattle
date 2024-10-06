package org.ssau.seabattle.configuration;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Data;

/**
 * TokenDetails
 */
@Data
@Builder(toBuilder = true) // TODO что делает toBuilder = true
public class TokenDetails {
  private Long userId;
  private String token;
  private LocalDate issuedDate;
  private LocalDate expiresDate;

}
