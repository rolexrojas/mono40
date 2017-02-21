package com.tpago.movil.ui.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.EditText;

import com.tpago.movil.R;
import com.tpago.movil.util.Strings;

/**
 * @author hecvasro
 */
public class TextInput extends EditText {
  private static final int[] STATE_ERROR = new int[] { R.attr.state_error };

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
    if (Strings.isNotEmpty(getError())) {
      mergeDrawableStates(state, STATE_ERROR);
    }
    return state;
  }

  @Override
  public void setError(CharSequence error, Drawable icon) {
    super.setError(error, icon);
    refreshDrawableState();
  }
}
