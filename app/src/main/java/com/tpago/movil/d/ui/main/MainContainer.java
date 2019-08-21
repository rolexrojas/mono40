package com.tpago.movil.d.ui.main;

import androidx.annotation.Nullable;

import com.tpago.movil.d.ui.SwitchableContainer;

/**
 * TODO
 *
 * @author hecvasro
 */
public interface MainContainer extends SwitchableContainer<DepMainComponent> {
  /**
   * TODO
   *
   * @param title
   *   TODO
   */
  void setTitle(@Nullable String title);
}
