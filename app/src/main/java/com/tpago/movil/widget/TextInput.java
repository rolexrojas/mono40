package com.tpago.movil.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;

import com.tpago.movil.util.Objects;

/**
 * @author hecvasro
 */
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
    if (Objects.isNull(erraticViewHelper)) {
      return super.onCreateDrawableState(extraSpace);
    } else {
      return erraticViewHelper.onCreateDrawableState(
        super.onCreateDrawableState(erraticViewHelper.getExtraSpace(extraSpace)));
    }
  }

  @Override
  public boolean checkIfErraticStateEnabled() {
    return erraticViewHelper.checkIfErraticStateEnabled();
  }

  @Override
  public void setErraticStateEnabled(boolean erraticStateEnabled) {
    erraticViewHelper.setErraticStateEnabled(erraticStateEnabled);
  }
}
