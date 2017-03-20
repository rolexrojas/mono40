package com.tpago.movil.dep.domain;

import android.support.annotation.NonNull;

import java.math.BigDecimal;

/**
 * {@link Product}'s balance representation.
 *
 * @author hecvasro
 */
public abstract class Balance {
  /**
   * Balance's value.
   */
  private final BigDecimal value;

  /**
   * Constructs a new balance.
   *
   * @param value
   *   Balance's value.
   */
  Balance(@NonNull BigDecimal value) {
    this.value = value;
  }

  /**
   * Gets the value of the balance.
   *
   * @return Balance's value.
   */
  @NonNull
  public final BigDecimal getValue() {
    return value;
  }

  @Override
  public String toString() {
    return Balance.class.getSimpleName() + ":{value=" + value + "'}";
  }
}
