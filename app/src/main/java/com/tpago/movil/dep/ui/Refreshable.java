package com.tpago.movil.dep.ui;

import android.support.annotation.Nullable;

import com.tpago.movil.dep.ui.view.widget.LoadIndicator;

/**
 * TODO
 *
 * @author hecvasro
 */
public interface Refreshable {
  /**
   * TODO
   *
   * @return TODO
   */
  @Nullable
  LoadIndicator getRefreshIndicator();
}
