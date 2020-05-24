package com.mono40.movil.time;

/**
 * @author hecvasro
 */
final class SystemClock implements Clock {

  static SystemClock create() {
    return new SystemClock();
  }

  private SystemClock() {
  }

  @Override
  public long getTime() {
    return System.currentTimeMillis();
  }
}
