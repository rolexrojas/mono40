package com.tpago.movil.util;

import static java.lang.String.format;

/**
 * Collection of helpers for {@link Object objects}.
 *
 * @author hecvasro
 */
public final class ObjectHelper {

  /**
   * Checks whether the given {@link Object object} is null or not.
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
   * Checks whether the given {@link Object object} is null.
   *
   * @param object
   *   {@link Object} that will be checked.
   *
   * @return True if the given {@link Object object} is not null, false otherwise.
   */
  public static boolean isNotNull(Object object) {
    return !isNull(object);
  }

  /**
   * Checks if the given {@link T object} is null. An {@link NullPointerException exception} will be
   * thrown if it's null.
   *
   * @param object
   *   {@link T Object} that will be checked.
   * @param argumentName
   *   {@link String} that will be used for the message of the {@link NullPointerException
   *   exception}.
   * @param <T>
   *   Type of object that will be checked.
   *
   * @return Given {@link T object} if it isn't null.
   *
   * @throws NullPointerException
   *   If {@code object} is null.
   */
  public static <T> T checkNotNull(T object, String argumentName) {
    if (isNull(object)) {
      throw new NullPointerException(format("isNull(%1$s)", argumentName));
    }
    return object;
  }

  private ObjectHelper() {
  }
}
