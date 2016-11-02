package com.gbh.movil;

import android.support.annotation.NonNull;

import java.util.Arrays;

/**
 * TODO
 *
 * @author hecvasro
 */
public final class Utils {
  private Utils() {
  }

  /**
   * TODO
   *
   * @param objects
   *   TODO
   *
   * @return TODO
   */
  public static int hashCode(@NonNull Object... objects) {
    return Arrays.hashCode(objects);
  }
}
