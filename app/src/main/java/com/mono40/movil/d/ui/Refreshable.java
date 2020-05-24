package com.mono40.movil.d.ui;

import androidx.annotation.Nullable;

import com.mono40.movil.d.ui.view.widget.LoadIndicator;

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
