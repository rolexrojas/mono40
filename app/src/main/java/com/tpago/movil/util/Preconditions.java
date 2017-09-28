package com.tpago.movil.util;

/**
 * @author hecvasro
 */
@Deprecated
public final class Preconditions {
  public static <T> T assertNotNull(T reference, String message) {
    if (Objects.checkIfNull(reference)) {
      throw new NullPointerException(message);
    }
    return reference;
  }

  private Preconditions() {
    throw new AssertionError("Cannot be instantiated");
  }
}
