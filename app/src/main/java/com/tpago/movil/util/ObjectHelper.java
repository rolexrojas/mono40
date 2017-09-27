package com.tpago.movil.util;

import static java.lang.String.format;

/**
 * Collection of helpers for {@link Object objects}.
 *
 * @author hecvasro
 */
public final class ObjectHelper {

  /**
   * Checks whether a given {@link Object object} is {@code null} or not.
   *
   * @param object
   *   {@link Object} that will be checked.
   *
   * @return True if the given {@link Object object} is {@code null}, false otherwise.
   */
  public static boolean isNull(Object object) {
    return object == null;
  }

  /**
   * Checks whether a given {@link Object object} is {@code null}.
   *
   * @param object
   *   {@link Object} that will be checked.
   *
   * @return True if the given {@link Object object} is not {@code null}, false otherwise.
   */
  public static boolean isNotNull(Object object) {
    return !isNull(object);
  }

  /**
   * Returns a given {@link T object} if it isn't {@code null}, or otherwise throws an {@link
   * NullPointerException exception} if it's {@code null}.
   *
   * @param object
   *   {@link T Object} that will be checked.
   * @param argumentName
   *   {@link String} that will be used for the message of the {@link NullPointerException
   *   exception}.
   *
   * @return Given {@link T object} if it isn't {@code null}.
   *
   * @throws NullPointerException
   *   If {@code object} is {@code null}.
   */
  public static <T> T checkNotNull(T object, String argumentName) {
    if (isNull(object)) {
      throw new NullPointerException(format("isNull(%1$s)", argumentName));
    }
    return object;
  }

  /**
   * Returns the first of two given {@link T objects} that is not {@code null}, if either is, or
   * otherwise throws an {@link NullPointerException exception}.
   *
   * @return The first of the two given {@link T objects} that is not {@code null}.
   */
  public static <T> T firstNonNull(T first, T second) {
    return isNull(first) ? checkNotNull(second, "second") : first;
  }

  private ObjectHelper() {
  }
}
