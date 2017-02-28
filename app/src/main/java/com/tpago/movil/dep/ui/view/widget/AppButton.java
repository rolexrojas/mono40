package com.tpago.movil.dep.ui.view.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * TODO
 *
 * @author hecvasro
 */
public class AppButton extends Button {
  public AppButton(Context context) {
    super(context);
  }

  public AppButton(Context context, AttributeSet attrs) {
    super(context, attrs, 0);
  }

  public AppButton(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override
  public void setEnabled(boolean enabled) {
    super.setEnabled(enabled);
    setAlpha(enabled ? 1.0F : 0.5F);
  }
}
