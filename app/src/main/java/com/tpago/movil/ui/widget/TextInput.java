package com.tpago.movil.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

import com.tpago.movil.R;

/**
 * @author hecvasro
 */
public class TextInput extends EditText {
  private static final int[] STATE_ERRATIC = new int[] { R.attr.state_erratic };

  private boolean erraticStateEnabled = false;

  public TextInput(Context context) {
    super(context);
  }

  public TextInput(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public TextInput(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override
  protected int[] onCreateDrawableState(int extraSpace) {
    int[] state = super.onCreateDrawableState(extraSpace + 1);
    if (erraticStateEnabled) {
      mergeDrawableStates(state, STATE_ERRATIC);
    }
    return state;
  }

  public boolean isErraticStateEnabled() {
    return erraticStateEnabled;
  }

  public void setErraticStateEnabled(boolean erraticStateEnabled) {
    this.erraticStateEnabled = erraticStateEnabled;
    this.refreshDrawableState();
  }
}
