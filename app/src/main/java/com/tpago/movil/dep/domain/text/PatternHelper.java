package com.tpago.movil.dep.domain.text;

import android.support.annotation.Nullable;
import android.util.Patterns;

/**
 * TODO
 *
 * @author hecvasro
 */
public final class PatternHelper {
  private PatternHelper() {
  }

  /**
   * TODO
   *
   * @param text
   *   TODO
   *
   * @return TODO
   */
  public static boolean isValidEmail(@Nullable CharSequence text) {
    return TextHelper.isNotEmpty(text) && Patterns.EMAIL_ADDRESS.matcher(text).matches();
  }

  /**
   * TODO
   *
   * @param text
   *   TODO
   *
   * @return TODO
   */
  public static boolean isValidPassword(@Nullable CharSequence text) {
    return TextHelper.isNotEmpty(text);
  }
}
