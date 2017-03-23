package com.tpago.movil.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.tpago.movil.util.Objects;

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
