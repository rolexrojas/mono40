package com.gbh.movil.domain.product;

import android.support.annotation.NonNull;

import com.gbh.movil.domain.Bank;

import java.math.BigDecimal;

/**
 * Loan representation.
 *
 * @author hecvasro
 */
public class Loan extends Product {
  Loan(@NonNull ProductIdentifier type, @NonNull String alias, @NonNull String number,
    @NonNull Bank bank, @NonNull String currency, @NonNull BigDecimal queryFee) {
    super(ProductCategory.LOAN, type, alias, number, bank, currency, queryFee);
  }

  @Override
  public String toString() {
    return Loan.class.getSimpleName() + ":{super=" + super.toString() + "}";
  }
}
