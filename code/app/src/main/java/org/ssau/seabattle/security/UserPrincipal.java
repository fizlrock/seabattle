package org.ssau.seabattle.configuration;

import java.security.Principal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * UserPrincipal
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPrincipal implements Principal {

  private Long id;
  private String username;
  @Override
  public String getName() {
    return "fizlrock_impl_principal";
  }

}
