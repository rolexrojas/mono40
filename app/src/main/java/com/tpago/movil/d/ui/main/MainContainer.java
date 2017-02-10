package com.tpago.movil.d.ui.main;

import android.support.annotation.Nullable;

import com.tpago.movil.d.ui.SwitchableContainer;

/**
 * TODO
 *
 * @author hecvasro
 */
public interface MainContainer extends SwitchableContainer<MainComponent> {
  /**
   * TODO
   *
   * @param title
   *   TODO
   */
  void setTitle(@Nullable String title);
}
