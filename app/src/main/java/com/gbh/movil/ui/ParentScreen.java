package com.gbh.movil.ui;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * TODO
 *
 * @author hecvasro
 */
public interface ParentScreen<C> extends Screen, Injectable<C> {
  /**
   * TODO
   *
   * @param subFragment
   *   TODO
   */
  void setSubScreen(@NonNull SubFragment<C> subFragment);

  /**
   * TODO
   *
   * @param title
   *   TODO
   */
  void setTitle(@Nullable String title);

}
