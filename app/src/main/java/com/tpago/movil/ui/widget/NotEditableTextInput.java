package com.tpago.movil.ui.widget;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.util.AttributeSet;

import com.tpago.movil.text.BaseTextWatcher;

/**
 * @author hecvasro
 */
public class NotEditableTextInput extends TextInput {
  public NotEditableTextInput(Context context) {
    super(context);
    initializeNotEditableTextInput(context, null, 0);
  }

  public NotEditableTextInput(Context context, AttributeSet attrs) {
    super(context, attrs);
    initializeNotEditableTextInput(context, attrs, 0);
  }

  public NotEditableTextInput(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    initializeNotEditableTextInput(context, attrs, defStyleAttr);
  }

  private void initializeNotEditableTextInput(Context c, AttributeSet attrs, int defStyleAttr) {
    // Sets the input type.
    setInputType(InputType.TYPE_NULL);
    setRawInputType(InputType.TYPE_CLASS_TEXT);
    // Makes the text selectable.
    setTextIsSelectable(true);
    // Adds a listener that gets notified each time the text changes.
    addTextChangedListener(new BaseTextWatcher() {
      @Override
      public void afterTextChanged(Editable s) {
        setSelection(s.length());
      }
    });
  }
}
