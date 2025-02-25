package com.tpago.movil.dep.widget;

import android.content.Context;
import androidx.appcompat.widget.AppCompatEditText;
import android.util.AttributeSet;

import com.tpago.movil.util.ObjectHelper;

/**
 * @author hecvasro
 */
@Deprecated
public class TextInput extends AppCompatEditText implements ErraticView {

  private ErraticViewHelper erraticViewHelper;

  public TextInput(Context context) {
    super(context);
    initializeTextInput();
  }

  public TextInput(Context context, AttributeSet attrs) {
    super(context, attrs);
    initializeTextInput();
  }

  public TextInput(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    initializeTextInput();
  }

  private void initializeTextInput() {
    erraticViewHelper = new ErraticViewHelper(this);
  }

  @Override
  protected int[] onCreateDrawableState(int extraSpace) {
    if (ObjectHelper.isNull(erraticViewHelper)) {
      return super.onCreateDrawableState(extraSpace);
    } else {
      return erraticViewHelper.onCreateDrawableState(
        super.onCreateDrawableState(erraticViewHelper.getExtraSpace(extraSpace))
      );
    }
  }

  @Override
  public boolean isErraticStateEnabled() {
    return erraticViewHelper.isErraticStateEnabled();
  }

  @Override
  public void setErraticStateEnabled(boolean erraticStateEnabled) {
    erraticViewHelper.setErraticStateEnabled(erraticStateEnabled);
  }
}
