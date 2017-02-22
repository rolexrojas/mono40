package com.tpago.movil.ui.widget;

import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;

/**
 * @author hecvasro
 */
public class NumPadTextInput extends TextInput {
  public NumPadTextInput(Context context) {
    super(context);
    initializeNumPadTextInput(context, null, 0);
  }

  public NumPadTextInput(Context context, AttributeSet attrs) {
    super(context, attrs);
    initializeNumPadTextInput(context, attrs, 0);
  }

  public NumPadTextInput(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    initializeNumPadTextInput(context, attrs, defStyleAttr);
  }

  private void initializeNumPadTextInput(Context c, AttributeSet attrs, int defStyleAttr) {
    // Sets the input type.
    setInputType(InputType.TYPE_NULL);
    setRawInputType(InputType.TYPE_CLASS_TEXT);
    // Makes the text selectable.
    setTextIsSelectable(true);
  }
}
