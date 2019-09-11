package com.tpago.movil.d.ui;

import androidx.annotation.Nullable;

import com.tpago.movil.d.ui.view.widget.LoadIndicator;

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
