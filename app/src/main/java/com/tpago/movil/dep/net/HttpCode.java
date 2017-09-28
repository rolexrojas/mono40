package com.tpago.movil.dep.net;

/**
 * @author hecvasro
 */
@Deprecated
public enum HttpCode {
  OK(200),
  MULTIPLE_CHOICES(300),
  BAD_REQUEST(400),
  INTERNAL_SERVER_ERROR(500);

  private final int value;

  public static boolean isSuccessCode(HttpCode code) {
    return code.getValue() >= 200 && code.getValue() < 300;
  }

  public static HttpCode find(int value) {
    for (HttpCode code : values()) {
      if (code.getValue() == value) {
        return code;
      }
    }
    if (value >= 200 && value < 300) {
      return OK;
    } else if (value >= 300 && value < 400) {
      return MULTIPLE_CHOICES;
    } else if (value >= 400 && value < 500) {
      return BAD_REQUEST;
    } else {
      return INTERNAL_SERVER_ERROR;
    }
  }

  HttpCode(int value) {
    this.value = value;
  }

  public final int getValue() {
    return value;
  }
}
