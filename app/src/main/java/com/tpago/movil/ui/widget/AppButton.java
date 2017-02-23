package com.tpago.movil.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * @author hecvasro
 */
public final class AppButton extends Button {
  public AppButton(Context context) {
    super(context);
  }

  public AppButton(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public AppButton(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override
  public void setEnabled(boolean enabled) {
    super.setEnabled(enabled);
    setAlpha(enabled ? 1.00F : 0.50F);
  }
}
