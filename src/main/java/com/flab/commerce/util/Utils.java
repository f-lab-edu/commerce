package com.flab.commerce.util;

import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

public class Utils {

  public static final PasswordEncoder PASSWORD_ENCODER = PasswordEncoderFactories.createDelegatingPasswordEncoder();

  private Utils() {
    throw new IllegalStateException("Utility class");
  }

  public static String encodePassword(String password) {
    return PASSWORD_ENCODER.encode(password);
  }
}
