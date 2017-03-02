package com.tpago.movil.dep.domain;

import android.support.annotation.NonNull;

import com.tpago.movil.Bank;

import java.math.BigDecimal;

/**
 * Account representation.
 *
 * @author hecvasro
 */
public class Account extends Product {
  Account(@NonNull ProductType type, @NonNull String alias, @NonNull String number,
    @NonNull Bank bank, @NonNull String currency, @NonNull BigDecimal queryFee,
    boolean paymentOption, boolean isDefault) {
    super(ProductCategory.ACCOUNT, type, alias, number, bank, currency, queryFee, paymentOption,
      isDefault);
  }

  @Override
  public String toString() {
    return Account.class.getSimpleName() + ":{super=" + super.toString() + "}";
  }
}
