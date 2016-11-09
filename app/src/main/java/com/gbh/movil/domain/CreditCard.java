package com.gbh.movil.domain;

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
    double queryFee) {
    super(AccountType.CREDIT_CARD, alias, currency, bank, queryFee);
  }

  @Override
  public String toString() {
    return CreditCard.class.getSimpleName() + ":{super=" + super.toString() + "}";
  }
}
