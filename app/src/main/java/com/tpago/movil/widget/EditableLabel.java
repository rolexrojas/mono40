package com.tpago.movil.widget;

import android.content.Context;
import android.util.AttributeSet;

import static com.tpago.movil.util.Objects.checkIfNull;

/**
 * @author hecvasro
 */
public class EditableLabel extends Label implements ErraticView {
  private ErraticViewHelper erraticViewHelper;

  public EditableLabel(Context context) {
    super(context);
    initializeLabel();
  }

  public EditableLabel(Context context, AttributeSet attrs) {
    super(context, attrs);
    initializeLabel();
  }

  public EditableLabel(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    initializeLabel();
  }

  private void initializeLabel() {
    erraticViewHelper = new ErraticViewHelper(this);
  }

  @Override
  protected int[] onCreateDrawableState(int extraSpace) {
    if (checkIfNull(erraticViewHelper)) {
      return super.onCreateDrawableState(extraSpace);
    } else {
      return erraticViewHelper.onCreateDrawableState(
        super.onCreateDrawableState(erraticViewHelper.getExtraSpace(extraSpace)));
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
