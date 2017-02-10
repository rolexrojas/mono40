package com.tpago.movil.d.domain;

import android.support.annotation.NonNull;

import java.math.BigDecimal;

/**
 * {@link Product}'s balance representation.
 *
 * @author hecvasro
 */
public abstract class Balance {
  /**
   * Balance's {@link ProductCategory category}.
   */
  private final ProductCategory category;
  /**
   * Balance's value.
   */
  private final BigDecimal value;

  /**
   * Constructs a new balance.
   *
   * @param category
   *   Balance's {@link ProductCategory category}.
   * @param value
   *   Balance's value.
   */
  Balance(@NonNull ProductCategory category, @NonNull BigDecimal value) {
    this.category = category;
    this.value = value;
  }

  /**
   * Gets the {@link ProductCategory category} of the balance.
   *
   * @return Balance's {@link ProductCategory category}.
   */
  @NonNull
  public final ProductCategory getCategory() {
    return category;
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
    return Balance.class.getSimpleName() + ":{category='" + category + "',value=" + value + "'}";
  }
}
