package com.gbh.movil.domain;

import android.support.annotation.NonNull;

import java.math.BigDecimal;

/**
 * Account representation.
 *
 * @author hecvasro
 */
public class Account extends Product {
  Account(@NonNull ProductIdentifier type, @NonNull String alias, @NonNull String number,
    @NonNull Bank bank, @NonNull String currency, @NonNull BigDecimal queryFee,
    boolean paymentOption) {
    super(ProductCategory.ACCOUNT, type, alias, number, bank, currency, queryFee, paymentOption);
  }

  @Override
  public String toString() {
    return Account.class.getSimpleName() + ":{super=" + super.toString() + "}";
  }
}
