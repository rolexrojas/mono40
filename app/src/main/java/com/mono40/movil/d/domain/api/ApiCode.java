package com.mono40.movil.d.domain.api;

import androidx.annotation.NonNull;

import com.mono40.movil.dep.net.HttpCode;

/**
 * {@link DepApiBridge API}'s code enumeration.
 *
 * @author hecvasro
 */
@Deprecated
public enum ApiCode {
  OK(HttpCode.OK.getValue()),
  MULTIPLE_CHOICES(HttpCode.MULTIPLE_CHOICES.getValue()),
  BAD_REQUEST(HttpCode.BAD_REQUEST.getValue()),
  BAD_REQUEST_UNAUTHORIZED(401),
  BAD_REQUEST_FORBIDDEN(403),
  BAD_REQUEST_NOT_FOUND(404),
  INTERNAL_SERVER_ERROR(HttpCode.INTERNAL_SERVER_ERROR.getValue());

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

  /**
   * TODO
   *
   * @return TODO
   */
  public final int getValue() {
    return value;
  }
}
