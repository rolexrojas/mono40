package com.tpago.movil.util;

/**
 * Functional interface that supplies an object of a given type.
 *
 * @param <T>
 *   Type of object that is supplied.
 *
 * @author hecvasro
 */
public interface Supplier<T> {

  /**
   * Supplies an {@link T object}.
   *
   * @return {@link T Object} supplied.
   */
  T get();
}
