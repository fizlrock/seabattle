package org.ssau.sandbox.auth.jwt;

import java.util.Base64;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.AccessLevel;

/**
 * JWTSecrets
 */
@Component
@Getter
public class JWTSecrets {

  @Value("${jwt.secret}")
  @Getter(AccessLevel.NONE)
  private String key_base64;

  @Value("${jwt.experation}")
  private Integer expInSeconds;

  @Value("${jwt.issuer}")
  private String issuer;

  private SecretKey key;

  @PostConstruct
  void init() {
    byte[] decoded_key = Base64.getDecoder().decode(key_base64);

    key = new SecretKey() {
      @Override
      public String getAlgorithm() {
        return "HmacSHA256";
      }

      @Override
      public byte[] getEncoded() {
        return decoded_key;
      }

      @Override
      public String getFormat() {
        return "RAW";
      }
    };
  }
}
