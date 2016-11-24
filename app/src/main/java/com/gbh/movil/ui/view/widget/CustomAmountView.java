package com.gbh.movil.ui.view.widget;

import android.content.Context;
import android.util.AttributeSet;

/**
 * TODO
 *
 * @author hecvasro
 */
public final class CustomAmountView extends AmountView {
  public CustomAmountView(Context context) {
    super(context);
  }

  public CustomAmountView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public CustomAmountView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override
  protected boolean alwaysShowCents() {
    return false;
  }

  /**
   * TODO
   */
  public final void pop() {
    // TODO
  }

  /**
   * TODO
   *
   * @param digit
   *   TODO
   */
  public final void pushDigit(int digit) {
    // TODO
  }

  public final void pushDot() {
    // TODO
  }
}
