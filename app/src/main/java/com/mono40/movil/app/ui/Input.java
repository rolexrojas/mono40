package com.mono40.movil.app.ui;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatEditText;

import com.mono40.movil.app.ui.util.Erratic;
import com.mono40.movil.app.ui.util.ErraticHelper;

/**
 * @author hecvasro
 */
public class Input extends AppCompatEditText implements Erratic {

  private ErraticHelper helper;

  public Input(Context context) {
    super(context);
    this.init();
  }

  public Input(Context context, AttributeSet attrs) {
    super(context, attrs);
    this.init();
  }

  public Input(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    this.init();
  }

  private void init() {
    this.helper = ErraticHelper.create(this);
  }

  @Override
  public boolean isErraticStateEnabled() {
    return this.helper.isErraticStateEnabled();
  }

  @Override
  public void setErraticStateEnabled(boolean enabled) {
    this.helper.setErraticStateEnabled(enabled);
  }

  @Override
  protected int[] onCreateDrawableState(int extraSpace) {
    return this.helper.createState(super.onCreateDrawableState(extraSpace + 1));
  }
}
