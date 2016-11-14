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
  public CreditCard(@NonNull String alias, @NonNull String number, @NonNull Bank bank,
    @NonNull String currency, double queryFee) {
    super(AccountType.CREDIT_CARD, alias, number, bank, currency, queryFee);
  }

  @Override
  public String toString() {
    return CreditCard.class.getSimpleName() + ":{super=" + super.toString() + "}";
  }
}
