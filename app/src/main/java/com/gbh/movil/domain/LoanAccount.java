package com.gbh.movil.domain;

import android.support.annotation.NonNull;

import java.math.BigDecimal;

/**
 * TODO
 *
 * @author hecvasro
 */
public class LoanAccount extends Account {
  LoanAccount(@NonNull AccountType type, @NonNull String alias, @NonNull String number,
    @NonNull Bank bank, @NonNull String currency, @NonNull BigDecimal queryFee) {
    super(AccountCategory.LOAN, type, alias, number, bank, currency, queryFee);
  }

  @Override
  public String toString() {
    return LoanAccount.class.getSimpleName() + ":{super=" + super.toString() + "}";
  }
}
