package com.tpago.movil.d.ui;

import android.support.annotation.NonNull;

/**
 * TODO
 *
 * @author hecvasro
 */
public interface ContainerChild<T extends Container<?>> {
  /**
   * TODO
   *
   * @return TODO
   */
  @NonNull
  T getContainer();
}
