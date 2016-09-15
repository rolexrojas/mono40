package com.tpago.movil.domain;

import android.support.annotation.NonNull;

/**
 * Bank account representation.
 *
 * @author hecvasro
 */
public final class BankAccount extends Account {
  /**
   * {@inheritDoc}
   */
  public BankAccount(@NonNull String alias, @NonNull String currency, @NonNull Bank bank,
    double queryFee, @NonNull String queryFeeDescription) {
    super(AccountType.BANK_ACCOUNT, alias, currency, bank, queryFee, queryFeeDescription);
  }
}
