/*
 * Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.ssau.sandbox.auth.jwt;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.lang.Collections;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * This converter takes a SignedJWT and extracts all information
 * contained to build an Authentication Object
 * The signed JWT has already been verified.
 *
 */
@Slf4j
@Component
public class ClaimsToAuthConverter implements Function<Claims, Mono<Authentication>> {

  @Override
  public Mono<Authentication> apply(Claims claims) {
    String username = claims.getSubject();

    String[] roles = claims.get("roles", String.class)
        .split(",");

    var grantedAuthorities = Stream.of(roles)
        .map(String::strip)
        .map(SimpleGrantedAuthority::new)
        .toList();

    return Mono.just(new UsernamePasswordAuthenticationToken(username, null, grantedAuthorities));
  }

}
