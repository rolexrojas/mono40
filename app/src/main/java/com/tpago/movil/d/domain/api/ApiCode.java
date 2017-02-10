package com.tpago.movil.d.domain.api;

import android.support.annotation.NonNull;

/**
 * {@link ApiBridge API}'s code enumeration.
 *
 * @author hecvasro
 */
public enum ApiCode {
  OK(200),
  BAD_REQUEST(400),
  UNAUTHORIZED(401),
  FORBIDDEN(403),
  NOT_FOUND(404),
  UNEXPECTED(500);

  private final int value;

  ApiCode(int value) {
    this.value = value;
  }

  /**
   * TODO
   *
   * @param value
   *   TODO
   *
   * @return TODO
   */
  @NonNull
  public static ApiCode fromValue(int value) {
    for (ApiCode apiCode : ApiCode.values()) {
      if (value == apiCode.value) {
        return apiCode;
      }
    }
    return UNEXPECTED;
  }

  /**
   * TODO
   *
   * @return TODO
   */
  public final int getValue() {
    return value;
  }
}
