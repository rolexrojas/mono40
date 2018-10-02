package com.tpago.movil.app.ui.util;

import android.view.View;

import com.tpago.movil.R;
import com.tpago.movil.util.ObjectHelper;

/**
 * @author hecvasro
 */
public final class ErraticHelper implements Erratic {

  private static final int[] STATE_ARRAY = new int[]{
    R.attr.state_erratic
  };

  public static ErraticHelper create(View view) {
    return new ErraticHelper(view);
  }

  private final View view;

  private boolean enabled = false;

  private ErraticHelper(View view) {
    this.view = ObjectHelper.checkNotNull(view, "view");
  }

  public final int[] createState(int[] state) {
    if (this.enabled) {
      final int n = state.length;
      int i = n - 1;
      while (i >= 0 && state[i] == 0) {
        i--;
      }
      System.arraycopy(STATE_ARRAY, 0, state, i + 1, STATE_ARRAY.length);
    }
    return state;
  }

  @Override
  public boolean isErraticStateEnabled() {
    return this.enabled;
  }

  @Override
  public void setErraticStateEnabled(boolean enabled) {
    final boolean changed = this.enabled != enabled;
    this.enabled = enabled;
    if (changed) {
      this.view.refreshDrawableState();
    }
  }
}
