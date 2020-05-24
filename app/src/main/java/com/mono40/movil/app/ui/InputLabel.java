package com.mono40.movil.app.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.mono40.movil.app.ui.util.Erratic;
import com.mono40.movil.app.ui.util.ErraticHelper;
import com.mono40.movil.util.ObjectHelper;

/**
 * @author hecvasro
 */
public final class InputLabel extends FrameLayout implements Erratic {

  private Label child;
  private ErraticHelper childHelper;

  public InputLabel(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  private void checkChildIsNotSet() {
    if (ObjectHelper.isNotNull(this.child)) {
      throw new IllegalStateException("ObjectHelper.isNull(this.child)");
    }
  }

  private void checkChildIsLabel(View child) {
    if (!(child instanceof Label)) {
      throw new IllegalArgumentException("!(child instanceof Label)");
    }
  }

  private void setChild(View child) {
    this.child = (Label) child;
    this.child.setLayoutParams(new LayoutParams(
      LayoutParams.MATCH_PARENT,
      LayoutParams.WRAP_CONTENT,
      Gravity.CENTER
    ));

    this.childHelper = ErraticHelper.create(this.child);
  }

  @Override
  public void addView(View child) {
    this.checkChildIsNotSet();
    this.checkChildIsLabel(child);
    super.addView(child);
    this.setChild(child);
  }

  @Override
  public void addView(View child, int index) {
    this.checkChildIsNotSet();
    this.checkChildIsLabel(child);
    super.addView(child, index);
    this.setChild(child);
  }

  @Override
  public void addView(View child, int width, int height) {
    this.checkChildIsNotSet();
    this.checkChildIsLabel(child);
    super.addView(child, width, height);
    this.setChild(child);
  }

  @Override
  public void addView(View child, ViewGroup.LayoutParams params) {
    this.checkChildIsNotSet();
    this.checkChildIsLabel(child);
    super.addView(child, params);
    this.setChild(child);
  }

  @Override
  public boolean isErraticStateEnabled() {
    return this.childHelper.isErraticStateEnabled();
  }

  @Override
  public void setErraticStateEnabled(boolean enabled) {
    this.childHelper.setErraticStateEnabled(enabled);
  }

  @Override
  protected int[] onCreateDrawableState(int extraSpace) {
    if (ObjectHelper.isNull(this.childHelper)) {
      return super.onCreateDrawableState(extraSpace);
    }
    return this.childHelper.createState(super.onCreateDrawableState(extraSpace + 1));
  }
}
