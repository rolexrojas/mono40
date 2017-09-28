package com.tpago.movil.widget;

import android.view.View;

import com.tpago.movil.R;

import static com.tpago.movil.util.Preconditions.assertNotNull;

/**
 * @author hecvasro
 */
@Deprecated
final class ErraticViewHelper implements ErraticView {
  private static final int[] ARRAY_DRAWABLE_STATE_ERRATIC = new int[] { R.attr.state_erratic };

  private static int[] mergeDrawableStateArrays(
    int[] baseStateArray,
    int[] additionalStateArray) {
    final int n = baseStateArray.length;
    int i = n - 1;
    while (i >= 0 && baseStateArray[i] == 0) {
      i--;
    }
    System.arraycopy(additionalStateArray, 0, baseStateArray, i + 1, additionalStateArray.length);
    return baseStateArray;
  }

  private final View view;

  private boolean erraticStateEnabled = false;

  ErraticViewHelper(View view) {
    this.view = assertNotNull(view, "view == null");
  }

  final int getExtraSpace(int extraSpace) {
    return extraSpace + 1;
  }

  final int[] onCreateDrawableState(int[] state) {
    if (erraticStateEnabled) {
      mergeDrawableStateArrays(state, ARRAY_DRAWABLE_STATE_ERRATIC);
    }
    return state;
  }

  @Override
  public boolean isErraticStateEnabled() {
    return erraticStateEnabled;
  }

  @Override
  public void setErraticStateEnabled(boolean erraticStateEnabled) {
    this.erraticStateEnabled = erraticStateEnabled;
    this.view.refreshDrawableState();
  }
}
