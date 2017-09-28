package com.tpago.movil.d.domain.pos;

import android.support.annotation.NonNull;

/**
 * {@link PosBridge POS}'s code enumeration.
 *
 * @author hecvasro
 */
@Deprecated
public enum PosCode {
  OK(1),
  EXPIRED_AUTH_CODE(1000),
  INCORRECT_AUTH_CODE(1001),
  INCORRECT_ALIAS(1002),
  UNREGISTERED_PRODUCT(1003),
  ALREADY_ACTIVATED_PRODUCT(1008),
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
