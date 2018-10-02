package com.tpago.movil.util.function;

/**
 * Functional interface that supplies an object of a given type.
 *
 * @param <T>
 *   SupportedTransactionType of object that is supplied.
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
