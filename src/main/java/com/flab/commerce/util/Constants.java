package com.flab.commerce.util;

public class Constants {

  public static final String JSON_PROCESSING_EXCEPTION_MESSAGE = "JSON을 처리할 수 없습니다";
  public static final String CONTENT_TYPE = "Content-Type";
  public static final String UTF_8 = "UTF-8";
  public static final String ROLE_USER = "ROLE_USER";
  public static final String MUST_NOT_BE_BLANK = "must not be blank";

  public static String LENGTH_MUST_BE_BETWEEN_MIN_AND_NAX(int min, int max) {
    return String.format("length must be between %d and %d", min, max);
  }
}
