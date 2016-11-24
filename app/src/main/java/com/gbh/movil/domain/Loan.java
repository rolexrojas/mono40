package com.gbh.movil.domain;

import android.support.annotation.NonNull;

import java.math.BigDecimal;

/**
 * Loan representation.
 *
 * @author hecvasro
 */
public class Loan extends Product {
  Loan(@NonNull ProductIdentifier type, @NonNull String alias, @NonNull String number,
    @NonNull Bank bank, @NonNull String currency, @NonNull BigDecimal queryFee,
    boolean paymentOption) {
    super(ProductCategory.LOAN, type, alias, number, bank, currency, queryFee, paymentOption);
  }

  @Override
  public String toString() {
    return Loan.class.getSimpleName() + ":{super=" + super.toString() + "}";
  }
}
