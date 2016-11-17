package com.gbh.movil.domain;

import android.support.annotation.NonNull;

import java.math.BigDecimal;

/**
 * TODO
 *
 * @author hecvasro
 */
public final class AccountCreator {
  /**
   * TODO
   *
   * @param type
   *   Account's {@link AccountType type}.
   * @param alias
   *   Account's alias.
   * @param number
   *   Account's number.
   * @param bank
   *   Account's {@link Bank bank}.
   * @param currency
   *   Account's currency.
   * @param queryFee
   *   Account's query fee.
   *
   * @return TODO
   */
  @NonNull
  public static Account create(@NonNull AccountType type, @NonNull String alias,
    @NonNull String number, @NonNull Bank bank, @NonNull String currency,
    @NonNull BigDecimal queryFee) {
    if (type.equals(AccountType.LOAN)) {
      return new LoanAccount(type, alias, number, bank, currency, queryFee);
    } else if (type.equals(AccountType.AMEX) || type.equals(AccountType.CC)
      || type.equals(AccountType.TBD)) {
      return new CreditCardAccount(type, alias, number, bank, currency, queryFee);
    } else {
      return new ElectronicAccount(type, alias, number, bank, currency, queryFee);
    }
  }
}
