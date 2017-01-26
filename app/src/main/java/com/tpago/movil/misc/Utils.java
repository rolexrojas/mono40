package com.tpago.movil.misc;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

/**
 * TODO
 *
 * @author hecvasro
 */
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

  @NonNull
  public static Date getTime(long milliseconds, boolean clearTime) {
    final Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(milliseconds);
    if (clearTime) {
      calendar.set(Calendar.HOUR, 0);
      calendar.set(Calendar.MINUTE, 0);
      calendar.set(Calendar.SECOND, 0);
      calendar.set(Calendar.MILLISECOND, 0);
    }
    return calendar.getTime();
  }

  @NonNull
  public static Date getTime(long milliseconds) {
    return getTime(milliseconds, false);
  }
}
