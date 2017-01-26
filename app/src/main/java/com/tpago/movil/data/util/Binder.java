package com.tpago.movil.data.util;

import android.support.annotation.NonNull;

/**
 * TODO
 *
 * @author hecvasro
 */
public interface Binder<I, H> {
  /**
   * TODO
   *
   * @param item
   *   TODO
   * @param holder
   *   TODO
   */
  void bind(@NonNull I item, @NonNull H holder);
}
