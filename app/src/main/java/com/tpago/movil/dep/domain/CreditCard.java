package com.tpago.movil.dep.domain;

import android.support.annotation.NonNull;

import com.tpago.movil.Bank;

import java.math.BigDecimal;

/**
 * Credit card representation.
 *
 * @author hecvasro
 */
@Deprecated
public class CreditCard extends Product {
  CreditCard(@NonNull ProductType type, @NonNull String alias, @NonNull String number,
    @NonNull Bank bank, @NonNull String currency, @NonNull BigDecimal queryFee,
    boolean paymentOption, boolean isDefault) {
    super(ProductCategory.CREDIT_CARD, type, alias, number, bank, currency, queryFee,
      paymentOption, isDefault);
  }

  @Override
  public String toString() {
    return CreditCard.class.getSimpleName() + ":{super=" + super.toString() + "}";
  }
}
