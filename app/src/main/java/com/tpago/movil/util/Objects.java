package com.tpago.movil.util;

/**
 * @author hecvasro
 */
public final class Objects {
  private static final Object NOTIFICATION = new Object();

  public static <T> boolean isNull(T reference) {
    return reference == null;
  }

  public static <T> boolean isNotNull(T reference) {
    return !isNull(reference);
  }

  public static <T> T defaultIfNull(T reference, T defaultValue) {
    return isNotNull(reference) ? reference : defaultValue;
  }

  public static Object notification() {
    return NOTIFICATION;
  }

  private Objects() {
    throw new AssertionError("Cannot be instantiated");
  }
}
