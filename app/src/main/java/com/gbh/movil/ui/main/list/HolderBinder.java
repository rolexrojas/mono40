package com.gbh.movil.ui.main.list;

import android.support.annotation.NonNull;

/**
 * TODO
 *
 * @author hecvasro
 */
public interface HolderBinder<I, VH extends Holder> {
  /**
   * TODO
   *
   * @param item
   *   TODO
   * @param holder
   *   TODO
   */
  void bind(@NonNull I item, @NonNull VH holder);
}
