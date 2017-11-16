package com.tpago.movil.d.domain.text;

import android.support.annotation.Nullable;
import android.text.TextUtils;

/**
 * TODO
 *
 * @author hecvasro
 */
@Deprecated
public final class TextHelper {
  private TextHelper() {
  }

  /**
   * TODO
   *
   * @param text
   *   TODO
   * @param length
   *   TODO
   *
   * @return TODO
   */
  public static boolean assertLength(@Nullable CharSequence text, int length) {
    return isNotEmpty(text) && text.length() == length;
  }

  /**
   * TODO
   *
   * @param text
   *   TODO
   *
   * @return TODo
   */
  public static boolean isDigitsOnly(@Nullable CharSequence text) {
    return isNotEmpty(text) && TextUtils.isDigitsOnly(text);
  }

  /**
   * TODO
   *
   * @param text
   *   TODO
   *
   * @return TODO
   */
  public static boolean isEmpty(@Nullable CharSequence text) {
    return TextUtils.isEmpty(text);
  }

  /**
   * TODO
   *
   * @param text
   *   TODO
   *
   * @return TODO
   */
  public static boolean isNotEmpty(@Nullable CharSequence text) {
    return !isEmpty(text);
  }
}
