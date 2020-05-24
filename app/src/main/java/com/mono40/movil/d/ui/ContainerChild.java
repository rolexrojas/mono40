package com.mono40.movil.d.ui;

import androidx.annotation.NonNull;

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
