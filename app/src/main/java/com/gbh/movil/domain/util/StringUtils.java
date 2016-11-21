package com.gbh.movil.domain.util;

import android.support.annotation.NonNull;

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
  public static String sanitize(@NonNull String string) {
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
  public static boolean matches(@NonNull String a, @NonNull String b) {
    return sanitize(a).contains(sanitize(b));
  }
}
