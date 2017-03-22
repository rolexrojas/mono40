package com.tpago.movil.dep.data.util;

/**
 * @author hecvasro
 */
public interface Binder<I, H> {
  void bind(I item, H holder);
}
