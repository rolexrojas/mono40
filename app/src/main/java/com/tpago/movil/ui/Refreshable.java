package com.tpago.movil.ui;

import android.support.annotation.Nullable;

import com.tpago.movil.ui.view.widget.LoadIndicator;

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
