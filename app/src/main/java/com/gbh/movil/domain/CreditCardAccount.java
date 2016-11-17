package com.gbh.movil.domain;

import android.support.annotation.NonNull;

import java.math.BigDecimal;

/**
 * TODO
 *
 * @author hecvasro
 */
public class CreditCardAccount extends Account {
  CreditCardAccount(@NonNull AccountType type, @NonNull String alias, @NonNull String number,
    @NonNull Bank bank, @NonNull String currency, @NonNull BigDecimal queryFee) {
    super(AccountCategory.CREDIT_CARD, type, alias, number, bank, currency, queryFee);
  }

  @Override
  public String toString() {
    return CreditCardAccount.class.getSimpleName() + ":{super=" + super.toString() + "}";
  }
}
