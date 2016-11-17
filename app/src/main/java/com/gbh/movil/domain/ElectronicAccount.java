package com.gbh.movil.domain;

import android.support.annotation.NonNull;

import java.math.BigDecimal;

/**
 * TODO
 *
 * @author hecvasro
 */
public class ElectronicAccount extends Account {
  ElectronicAccount(@NonNull AccountType type, @NonNull String alias, @NonNull String number,
    @NonNull Bank bank, @NonNull String currency, @NonNull BigDecimal queryFee) {
    super(AccountCategory.ELECTRONIC, type, alias, number, bank, currency, queryFee);
  }

  @Override
  public String toString() {
    return ElectronicAccount.class.getSimpleName() + ":{super=" + super.toString() + "}";
  }
}
