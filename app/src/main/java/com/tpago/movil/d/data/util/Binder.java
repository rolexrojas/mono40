package com.tpago.movil.d.data.util;

/**
 * @author hecvasro
 */
@Deprecated
public interface Binder<I, H> {
  void bind(I item, H holder);
}
