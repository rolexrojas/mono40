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
  public SavingsAccount(@NonNull String alias, @NonNull String number, @NonNull Bank bank,
    @NonNull String currency, double queryFee) {
    super(AccountType.SAVINGS, alias, number, bank, currency, queryFee);
  }

  @Override
  public String toString() {
    return SavingsAccount.class.getSimpleName() + ":{super=" + super.toString() + "}";
  }
}
