package com.tpago.movil.dep.ui.main;

import android.support.annotation.Nullable;

import com.tpago.movil.dep.ui.SwitchableContainer;

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
