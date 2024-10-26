
package org.ssau.sandbox.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class LoginAndRegistrationController {

  /**
   * Common login endpoint is also available for basic authentication
   *
   * @return A publisher serving a message stating successful log in
   */
  @GetMapping("/token")
  public ResponseEntity<Void> login() {
    return ResponseEntity.accepted().build();
  }

  /**
   * A restricted endpoint requiring consumers to be authenticated and also
   * have the right roles for this resource
   *
   * @return A publisher serving a message when access is granted
  //  */
  // @GetMapping("/api/private")
  // @PreAuthorize("hasRole('USER')")
  // public Flux<FormattedMessage> privateMessage() {
  //   return messageService.getCustomMessage("User");
  // }

  // /**
  //  * A restricted endpoint requiring consumers to be authenticated and also
  //  * have the right roles for this resource
  //  *
  //  * @return A publisher serving a message when access is granted
  //  */
  // @GetMapping("/api/admin")
  // @PreAuthorize("hasRole('ADMIN')")
  // public Flux<FormattedMessage> privateMessageAdmin() {
  //   return messageService.getCustomMessage("Admin");
  // }

  // /**
  //  * A restricted endpoint requiring consumers to be authenticated and also
  //  * have the right roles for this resource
  //  *
  //  * @return A publisher serving a message when access is granted
  //  */
  // @GetMapping("/api/guest")
  // @PreAuthorize("hasRole('GUEST')")
  // public Flux<FormattedMessage> privateMessageGuest() {
  //   return messageService.getCustomMessage("Guest");
  // }
}
