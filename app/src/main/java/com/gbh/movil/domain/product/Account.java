package com.gbh.movil.domain.product;

import android.support.annotation.NonNull;

import com.gbh.movil.domain.Bank;

import java.math.BigDecimal;

/**
 * Account representation.
 *
 * @author hecvasro
 */
public class Account extends Product {
  Account(@NonNull ProductIdentifier type, @NonNull String alias, @NonNull String number,
    @NonNull Bank bank, @NonNull String currency, @NonNull BigDecimal queryFee) {
    super(ProductCategory.ACCOUNT, type, alias, number, bank, currency, queryFee);
  }

  @Override
  public String toString() {
    return Account.class.getSimpleName() + ":{super=" + super.toString() + "}";
  }
}
