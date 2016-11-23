package com.gbh.movil.domain.util;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gbh.movil.Utils;

/**
 * TODO
 *
 * @author hecvasro
 */
public final class StringUtils {
  private StringUtils() {
  }

  /**
   * TODO
   *
   * @param string
   *   TODO
   *
   * @return TODO
   */
  private static String sanitize(@NonNull String string) {
    return string.toUpperCase().replaceAll("[^0-9A-z]", "");
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
