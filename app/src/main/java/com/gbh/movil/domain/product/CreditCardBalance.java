package com.gbh.movil.domain.product;

import android.support.annotation.NonNull;

import java.math.BigDecimal;

/**
 * {@link CreditCard Credit card}'s balance representation.
 *
 * @author hecvasro
 */
public class CreditCardBalance extends Balance {
  /**
   * {@link CreditCard Credit card}'s available amount.
   */
  private final BigDecimal available;

  /**
   * Constructs a balance of a {@link CreditCard credit card}.
   *
   * @param value
   *   {@link CreditCard Credit card}'s total amount.
   * @param available
   *   {@link CreditCard Credit card}'s available amount.
   */
  public CreditCardBalance(@NonNull BigDecimal value, @NonNull BigDecimal available) {
    super(ProductCategory.CREDIT_CARD, value);
    this.available = available;
  }

  /**
   * Gets the available amount of the {@link CreditCard credit card}.
   *
   * @return {@link CreditCard Credit cards}'s available amount.
   */
  @NonNull
  public final BigDecimal getAvailable() {
    return available;
  }
}
