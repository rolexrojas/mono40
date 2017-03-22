package com.tpago.movil.d.domain;

import android.support.annotation.Nullable;

import com.tpago.movil.d.domain.text.TextHelper;

/**
 * @author hecvasro
 */
public final class Pin {
  /**
   * TODO
   */
  public static final int LENGTH = 4;

  private Pin() {
  }

  /**
   * TODO
   *
   * @param text
   *   TODO
   *
   * @return TODO
   */
  public static boolean isValid(@Nullable CharSequence text) {
    return TextHelper.assertLength(text, LENGTH) && TextHelper.isDigitsOnly(text);
  }
}
