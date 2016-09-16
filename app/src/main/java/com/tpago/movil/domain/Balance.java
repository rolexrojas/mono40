package com.tpago.movil.domain;

import android.support.annotation.NonNull;

/**
 * Account balance representation.
 *
 * @author hecvasro
 */
public class Balance {
  /**
   * Balance's value.
   */
  private final double value;

  /**
   * Balance's description.
   */
  private final String description;

  /**
   * Constructs a new balance.
   *
   * @param value
   *   Balance's value.
   * @param description
   *   Balance's description
   */
  public Balance(double value, @NonNull String description) {
    this.value = value;
    this.description = description;
  }

  /**
   * Gets the value of the balance.
   *
   * @return Balance's value.
   */
  public final double getValue() {
    return value;
  }

  /**
   * Gets the description of the balance.
   *
   * @return Balance's description.
   */
  @NonNull
  public final String getDescription() {
    return description;
  }
}
