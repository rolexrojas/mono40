package com.tpago.movil.util;

/**
 * @author hecvasro
 */
public final class Preconditions {
  public static <T> T checkNotNull(T reference, String message) {
    if (Objects.isNull(reference)) {
      throw new NullPointerException(message);
    }
    return reference;
  }

  private Preconditions() {
    throw new AssertionError("Cannot be instantiated");
  }
}
