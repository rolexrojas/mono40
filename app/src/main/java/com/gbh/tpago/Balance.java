package com.gbh.tpago;

import android.support.annotation.NonNull;

/**
 * TODO
 *
 * @author hecvasro
 */
public final class Balance {
  /**
   * TODO
   */
  private final long creationDate;

  /**
   * TODO
   */
  private final double value;

  /**
   * TODO
   */
  private final String description;

  /**
   * TODO
   *
   * @param value TODO
   * @param description TODO
   */
  public Balance(double value, @NonNull String description) {
    this.creationDate = System.currentTimeMillis();
    this.value = value;
    this.description = description;
  }

  /**
   * TODO
   *
   * @return TODO
   */
  public final boolean isExpired() {
    // TODO
    return true;
  }

  /**
   * TODO
   *
   * @return TODO
   */
  public final double getValue() {
    return value;
  }

  /**
   * TODO
   *
   * @return TODO
   */
  @NonNull
  public final String getDescription() {
    return description;
  }
}
