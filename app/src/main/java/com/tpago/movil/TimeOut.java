package com.tpago.movil;

import java.util.concurrent.TimeUnit;

/**
 * @author hecvasro
 */
@Deprecated
public enum TimeOut {
  ONE(1L),
  TWO(2L),
  THREE(3L),
  FOUR(4L),
  FIVE(5L),
  TEN(10L);

  public static TimeUnit getUnit() {
    return TimeUnit.MINUTES;
  }

  private final long value;

  TimeOut(long value) {
    this.value = value;
  }

  public final long getValue() {
    return value;
  }
}
