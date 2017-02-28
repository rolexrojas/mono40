package com.tpago.movil.dep.domain;

import android.support.annotation.NonNull;

import java.math.BigDecimal;

/**
 * {@link Account}'s balance representation.
 *
 * @author hecvasro
 */
public class AccountBalance extends Balance {
  /**
   * {@link Account}'s available amount.
   */
  private final BigDecimal available;

  /**
   * Constructs a balance for an {@link Account account}.
   *
   * @param total
   *   {@link Account}'s total amount.
   * @param available
   *   {@link Account}'s available amount.
   */
  public AccountBalance(@NonNull BigDecimal total, @NonNull BigDecimal available) {
    super(ProductCategory.ACCOUNT, total);
    this.available = available;
  }

  /**
   * Gets the available amount of the {@link Account account}.
   *
   * @return {@link Account}'s available amount.
   */
  @NonNull
  public final BigDecimal getAvailable() {
    return available;
  }
}
