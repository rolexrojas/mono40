package com.gbh.movil.ui;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * TODO
 *
 * @author hecvasro
 */
public interface Container<C> {
  /**
   * TODO
   *
   * @return TODO
   */
  @Nullable
  C getComponent();

  /**
   * TODO
   *
   * @param fragment
   *   TODO
   */
  void setSubScreen(@NonNull SubFragment<? extends Container<C>> fragment);
}
