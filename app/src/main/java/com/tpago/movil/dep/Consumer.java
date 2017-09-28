package com.tpago.movil.dep;

/**
 * Functional interface that accepts an object of a given type.
 *
 * @param <T>
 *   Type of object that is accepted.
 *
 * @author hecvasro
 */
@Deprecated
public interface Consumer<T> {

  /**
   * Accepts an object.
   *
   * @param t
   *   Object that will be accepted.
   */
  void accept(T t);
}
