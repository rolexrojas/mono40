package com.tpago.movil.util;

/**
 * @author hecvasro
 */
public final class Objects {
  private Objects() {
    throw new AssertionError("Cannot be instantiated");
  }

  public static <T> boolean isNull(T reference) {
    return reference == null;
  }

  public static <T> boolean isNotNull(T reference) {
    return !isNull(reference);
  }

  public static <T> void checkNotNull(T reference, String message) {
    if (isNull(reference)) {
      throw new NullPointerException(message);
    }
  }

  public static <T> T defaultIfNull(T reference, T defaultValue) {
    return isNotNull(reference) ? reference : defaultValue;
  }
}
