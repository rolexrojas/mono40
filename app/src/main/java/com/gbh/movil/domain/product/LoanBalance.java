package com.gbh.movil.domain.product;

import android.support.annotation.NonNull;

import java.math.BigDecimal;

/**
 * {@link Loan}'s balance representation.
 *
 * @author hecvasro
 */
public class LoanBalance extends Balance {
  /**
   * {@link Loan}'s fee.
   */
  private final BigDecimal fee;

  /**
   * Constructs a new balance of a {@link Loan loan}.
   *
   * @param value
   *   {@link Loan}'s principal.
   * @param fee
   *   {@link Loan}'s fee.
   */
  public LoanBalance(@NonNull BigDecimal value, @NonNull BigDecimal fee) {
    super(ProductCategory.LOAN, value);
    this.fee = fee;
  }

  /**
   * Gets the fee of the {@link Loan loan}.
   *
   * @return {@link Loan}'s fee.
   */
  @NonNull
  public final BigDecimal getFee() {
    return fee;
  }
}
