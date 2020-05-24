package com.mono40.movil.d.domain;

import androidx.annotation.Nullable;

import com.mono40.movil.d.domain.text.TextHelper;

/**
 * @author hecvasro
 */
@Deprecated
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
