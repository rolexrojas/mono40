package com.tpago.movil.d.domain.text;

import androidx.annotation.Nullable;
import android.util.Patterns;

/**
 * TODO
 *
 * @author hecvasro
 */
@Deprecated
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
