package com.tpago.movil.util.function;

/**
 * Functional interface that accepts two (2) objects of a two (2) given types.
 *
 * @param <T1>
 *   First value type.
 * @param <T2>
 *   Second value type.
 *
 * @author hecvasro
 */
public interface BiConsumer<T1, T2> {

  /**
   * Accepts an object.
   *
   * @param t1
   *   First value.
   * @param t2
   *   Second value.
   */
  void accept(T1 t1, T2 t2);
}
