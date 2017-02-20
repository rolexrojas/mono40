package com.tpago.movil.util;

/**
 * @author hecvasro
 */
public final class Preconditions {
  private Preconditions() {
    throw new AssertionError("Cannot be instantiated");
  }

  public static <T> T checkNotNull(T reference, String message) {
    if (Objects.isNull(reference)) {
      throw new NullPointerException(message);
    }
    return reference;
  }
}
