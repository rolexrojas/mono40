package com.gbh.movil.domain.product;

import android.support.annotation.NonNull;

import com.gbh.movil.domain.Bank;

import java.math.BigDecimal;

/**
 * Credit card representation.
 *
 * @author hecvasro
 */
public class CreditCard extends Product {
  CreditCard(@NonNull ProductIdentifier type, @NonNull String alias, @NonNull String number,
    @NonNull Bank bank, @NonNull String currency, @NonNull BigDecimal queryFee) {
    super(ProductCategory.CREDIT_CARD, type, alias, number, bank, currency, queryFee);
  }

  @Override
  public String toString() {
    return CreditCard.class.getSimpleName() + ":{super=" + super.toString() + "}";
  }
}
