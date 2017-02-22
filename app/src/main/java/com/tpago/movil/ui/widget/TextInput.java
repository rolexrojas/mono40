package com.tpago.movil.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

import com.tpago.movil.R;

/**
 * @author hecvasro
 */
public class TextInput extends EditText {
  private static final int[] STATE_ERROR = new int[] { R.attr.state_error };

  private boolean errorStateEnabled = false;

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
    if (errorStateEnabled) {
      mergeDrawableStates(state, STATE_ERROR);
    }
    return state;
  }

  public boolean isErrorStateEnabled() {
    return errorStateEnabled;
  }

  public void setErrorStateEnabled(boolean errorStateEnabled) {
    this.errorStateEnabled = errorStateEnabled;
    this.refreshDrawableState();
  }
}
