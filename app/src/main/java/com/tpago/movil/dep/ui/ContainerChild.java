package com.tpago.movil.dep.ui;

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
