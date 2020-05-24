package com.mono40.movil.d.domain.util;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mono40.movil.d.domain.text.TextHelper;
import com.mono40.movil.util.ObjectHelper;

/**
 * TODO
 * <p>
 * {@link TextHelper} must be used instead.
 *
 * @author hecvasro
 */
@Deprecated
public final class StringUtils {

  private StringUtils() {
  }

  /**
   * TODO
   *
   * @param s
   *   TODO
   *
   * @return TODO
   */
  public static String sanitize(@NonNull String s) {
    return s.toUpperCase()
      .replaceAll("[^0-9A-z]", "");
  }

  /**
   * TODO
   *
   * @param a
   *   TODO
   * @param b
   *   TODO
   *
   * @return TODO
   */
  public static boolean matches(@NonNull String a, @Nullable String b) {
    return ObjectHelper.isNull(b) || sanitize(a).contains(sanitize(b));
  }
}
