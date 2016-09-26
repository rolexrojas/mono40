package com.gbh.movil.ui.main;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * TODO
 *
 * @author hecvasro
 */
public interface ParentScreen {
  /**
   * TODO
   *
   * @return TODO
   */
  @Nullable
  MainComponent getComponent();

  /**
   * TODO
   *
   * @param subFragment
   *   TODO
   */
  void setSubScreen(@NonNull SubFragment subFragment);

  /**
   * TODO
   *
   * @param title
   *   TODO
   */
  void setTitle(@Nullable String title);
}
