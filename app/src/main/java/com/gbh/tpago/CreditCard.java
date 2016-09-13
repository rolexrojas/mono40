package com.gbh.tpago;

import android.support.annotation.NonNull;

/**
 * TODO
 *
 * @author hecvasro
 */
public final class CreditCard extends Account {
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
  public CreditCard(@NonNull String alias, @NonNull String currency, @NonNull String bank,
    double queryFee, @NonNull String queryFeeDescription, @NonNull String balanceUrl) {
    super(AccountType.CREDIT_CARD, alias, currency, bank, queryFee, queryFeeDescription,
      balanceUrl);
  }
}
