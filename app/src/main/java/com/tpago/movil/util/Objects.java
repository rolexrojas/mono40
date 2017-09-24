package com.tpago.movil.util;

/**
 * @author hecvasro
 */
@Deprecated
public final class Objects {
  private static final Object NOTIFICATION = new Object();

  public static <T> boolean checkIfNull(T reference) {
    return reference == null;
  }

  public static <T> boolean checkIfNotNull(T reference) {
    return !checkIfNull(reference);
  }

  public static <T> T getDefaultIfNull(T reference, T defaultValue) {
    return checkIfNotNull(reference) ? reference : defaultValue;
  }

  public static Object notification() {
    return NOTIFICATION;
  }

  private Objects() {
    throw new AssertionError("Cannot be instantiated");
  }
}
