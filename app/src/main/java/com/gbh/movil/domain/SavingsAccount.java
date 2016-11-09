package com.gbh.movil.domain;

import android.support.annotation.NonNull;

/**
 * Bank account representation.
 *
 * @author hecvasro
 */
public class SavingsAccount extends Account {
  /**
   * {@inheritDoc}
   */
  public SavingsAccount(@NonNull String alias, @NonNull String currency, @NonNull Bank bank,
    double queryFee, @NonNull String queryFeeDescription) {
    super(AccountType.SAVINGS, alias, currency, bank, queryFee, queryFeeDescription);
  }

  @Override
  public String toString() {
    return SavingsAccount.class.getSimpleName() + ":{super=" + super.toString() + "}";
  }
}
