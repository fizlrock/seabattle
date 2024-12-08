package org.ssau.sandbox.auth.basic;

import java.util.Base64;
import java.util.function.Function;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class BasicAuthenticationConverter implements ServerAuthenticationConverter {

  @Override
  public Mono<Authentication> convert(ServerWebExchange exchange) {

    var auth_headers = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION);

    if (auth_headers == null)
      return Mono.empty();
    if (auth_headers.size() > 1)
      throw new BadCredentialsException("Несколько заголовков Authorization");

    var header_value = auth_headers.get(0);

    if (!header_value.startsWith("Basic"))
      return Mono.empty();

    var userpass_base64 = header_value.substring(6);

    var userpass = new String(Base64.getDecoder().decode(userpass_base64));

    var user = userpass.split(":")[0];
    var pass = userpass.split(":")[1];

    return Mono.just(new UsernamePasswordAuthenticationToken(user, pass));

  }

}
