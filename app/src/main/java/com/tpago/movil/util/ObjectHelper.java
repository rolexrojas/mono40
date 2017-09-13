package com.tpago.movil.util;

/**
 * Collection of helpers for {@link Object objects}.
 *
 * @author hecvasro
 */
public final class ObjectHelper {

  /**
   * Checks if the given {@link Object object} is null or not.
   *
   * @param object
   *   {@link Object} that will be checked.
   *
   * @return True if the given {@link Object object} is null, false otherwise.
   */
  public static boolean isNull(Object object) {
    return object == null;
  }

  /**
   * Checks if the given {@link Object object} is null.
   *
   * @param object
   *   {@link Object} that will be checked.
   *
   * @return True if the given {@link Object object} is not null, false otherwise.
   */
  public static boolean isNotNull(Object object) {
    return !isNull(object);
  }

  private ObjectHelper() {
  }
}
