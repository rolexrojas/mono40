package com.tpago.movil.domain;

import android.support.annotation.NonNull;

/**
 * Credit card representation.
 *
 * @author hecvasro
 */
public class CreditCard extends Account {
  /**
   * {@inheritDoc}
   */
  public CreditCard(@NonNull String alias, @NonNull String currency, @NonNull Bank bank,
    double queryFee, @NonNull String queryFeeDescription, @NonNull String queryBalanceUrl) {
    super(AccountType.CREDIT_CARD, alias, currency, bank, queryFee, queryFeeDescription,
      queryBalanceUrl);
  }
}
