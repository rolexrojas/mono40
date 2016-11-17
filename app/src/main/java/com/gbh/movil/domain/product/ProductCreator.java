package com.gbh.movil.domain.product;

import android.support.annotation.NonNull;

import com.gbh.movil.domain.Bank;

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
   *   Product's query fee.
   *
   * @return TODO
   */
  @NonNull
  public static Product create(@NonNull ProductIdentifier identifier, @NonNull String alias,
    @NonNull String number, @NonNull Bank bank, @NonNull String currency,
    @NonNull BigDecimal queryFee) {
    if (identifier.equals(ProductIdentifier.LOAN)) {
      return new Loan(identifier, alias, number, bank, currency, queryFee);
    } else if (identifier.equals(ProductIdentifier.AMEX) || identifier.equals(ProductIdentifier.CC)
      || identifier.equals(ProductIdentifier.TBD)) {
      return new CreditCard(identifier, alias, number, bank, currency, queryFee);
    } else {
      return new Account(identifier, alias, number, bank, currency, queryFee);
    }
  }
}
