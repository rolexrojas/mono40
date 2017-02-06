package com.tpago.movil.ui;

import android.support.annotation.NonNull;

/**
 * TODO
 *
 * @author hecvasro
 */
public interface SwitchableContainer<C> extends Container<C> {
  /**
   * TODO
   *
   * @param fragment
   *   TODO
   * @param addToBackStack
   *   TODO
   * @param animate
   *   TODO
   */
  void setChildFragment(@NonNull ChildFragment<? extends Container<C>> fragment, boolean addToBackStack,
    boolean animate);
}
