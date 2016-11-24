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
   *   Product's {@link ProductIdentifier identifier}.
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
  public static Product create(@NonNull ProductIdentifier identifier, @NonNull String alias,
    @NonNull String number, @NonNull Bank bank, @NonNull String currency,
    @NonNull BigDecimal queryFee, boolean paymentOption) {
    if (identifier.equals(ProductIdentifier.LOAN)) {
      return new Loan(identifier, alias, number, bank, currency, queryFee, paymentOption);
    } else if (identifier.equals(ProductIdentifier.AMEX) || identifier.equals(ProductIdentifier.CC)
      || identifier.equals(ProductIdentifier.TBD)) {
      return new CreditCard(identifier, alias, number, bank, currency, queryFee, paymentOption);
    } else {
      return new Account(identifier, alias, number, bank, currency, queryFee, paymentOption);
    }
  }
}
