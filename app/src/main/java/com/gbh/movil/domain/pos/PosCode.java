package com.gbh.movil.domain.pos;

import android.support.annotation.NonNull;

/**
 * {@link PosBridge POS}'s code enumeration.
 *
 * @author hecvasro
 */
public enum PosCode {
  OK(1),
  EXPIRED_AUTH_CODE(1000),
  INCORRECT_AUTH_CODE(1001),
  INCORRECT_ALIAS(1002),
  UNREGISTERED_PRODUCT(1003),
  UNEXPECTED(5000);

  private final int value;

  PosCode(int value) {
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
  public static PosCode fromValue(int value) {
    for (PosCode apiCode : PosCode.values()) {
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
