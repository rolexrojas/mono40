package com.tpago.movil.dep.domain.util;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tpago.movil.dep.domain.text.TextHelper;
import com.tpago.movil.dep.misc.Utils;

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
    return s.toUpperCase().replaceAll("[^0-9A-z]", "");
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
    return Utils.isNull(b) || sanitize(a).contains(sanitize(b));
  }
}
