package com.gbh.movil.domain;

import android.support.annotation.NonNull;

import java.math.BigDecimal;

/**
 * TODO
 *
 * @author hecvasro
 */
public final class ProductCreator {
  /**
   * TODO
   *
   * @param identifier
   *   Product's {@link ProductType identifier}.
   * @param alias
   *   Product's alias.
   * @param number
   *   Product's number.
   * @param bank
   *   Product's {@link Bank bank}.
   * @param currency
   *   Product's currency.
   * @param queryFee
   *   Cost of querying the balance.
   * @param paymentOption
   *   Indicates whether can be used as a payment option or not.
   *
   * @return TODO
   */
  @NonNull
  public static Product create(@NonNull ProductType identifier, @NonNull String alias,
    @NonNull String number, @NonNull Bank bank, @NonNull String currency,
    @NonNull BigDecimal queryFee, boolean paymentOption, boolean isDefault) {
    if (currency.equals("DOP")) {
      currency = "RD$";
    }
    if (identifier.equals(ProductType.LOAN)) {
      return new Loan(identifier, alias, number, bank, currency, queryFee, paymentOption,
        isDefault);
    } else if (identifier.equals(ProductType.AMEX) || identifier.equals(ProductType.CC)
      || identifier.equals(ProductType.TBD)) {
      return new CreditCard(identifier, alias, number, bank, currency, queryFee, paymentOption,
        isDefault);
    } else {
      return new Account(identifier, alias, number, bank, currency, queryFee, paymentOption,
        isDefault);
    }
  }
}
