package com.gbh.movil.domain;

import android.support.annotation.NonNull;

import java.math.BigDecimal;

/**
 * Account balance representation.
 *
 * @author hecvasro
 */
public class Balance {
  /**
   * Balance's total amount.
   */
  private final BigDecimal total;
  /**
   * Balance's available amount.
   */
  private final BigDecimal available;
  /**
   * Balance's description.
   */
  private final String description;

  /**
   * Constructs a new balance.
   *
   * @param total
   *   Balance's total amount.
   * @param available
   *   Balance's available amount.
   * @param description
   *   Balance's description
   */
  public Balance(@NonNull BigDecimal total, @NonNull BigDecimal available,
    @NonNull String description) {
    this.total = total;
    this.available = available;
    this.description = description;
  }

  /**
   * Gets the total amount of the balance.
   *
   * @return Balance's total amount.
   */
  @NonNull
  public final BigDecimal getTotal() {
    return total;
  }

  /**
   * Gets the available amount of the balance.
   *
   * @return Balance's available amount.
   */
  @NonNull
  public final BigDecimal getAvailable() {
    return available;
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

  @Override
  public String toString() {
    return Balance.class.getSimpleName() + ":{total=" + total + ",available=" + available
      + ",description='" + description + "'}";
  }
}
