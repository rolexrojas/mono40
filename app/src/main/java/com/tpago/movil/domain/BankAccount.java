package com.tpago.movil.domain;

import android.support.annotation.NonNull;

/**
 * TODO
 *
 * @author hecvasro
 */
public final class BankAccount extends Account {
  /**
   * TODO
   *
   * @param alias
   *   TODO
   * @param currency
   *   TODO
   * @param bank
   *   TODO
   * @param queryFee
   *   TODO
   * @param queryFeeDescription
   *   TODO
   * @param balanceUrl
   *   TODO
   */
  public BankAccount(@NonNull String alias, @NonNull String currency, @NonNull String bank,
    double queryFee, @NonNull String queryFeeDescription, @NonNull String balanceUrl) {
    super(AccountType.BANK_ACCOUNT, alias, currency, bank, queryFee, queryFeeDescription, balanceUrl);
  }
}
