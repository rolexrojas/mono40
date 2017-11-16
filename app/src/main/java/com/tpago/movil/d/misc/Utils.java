package com.tpago.movil.d.misc;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Arrays;

/**
 * @author hecvasro
 */
@Deprecated
public final class Utils {

  private Utils() {
  }

  public static boolean isNull(@Nullable Object object) {
    return object == null;
  }

  public static boolean isNotNull(@Nullable Object object) {
    return !isNull(object);
  }

  public static int hashCode(@NonNull Object... objects) {
    return Arrays.hashCode(objects);
  }
}
