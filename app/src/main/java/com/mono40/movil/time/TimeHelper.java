package com.mono40.movil.time;

import java.util.concurrent.TimeUnit;

/**
 * @author hecvasro
 */
public final class TimeHelper {

  public static long toDays(long duration) {
    return TimeUnit.MILLISECONDS.toDays(duration);
  }

  private TimeHelper() {
  }
}
