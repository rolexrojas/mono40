package com.tpago.movil.domain;

import android.support.annotation.NonNull;

import java.util.List;

import rx.Observable;

/**
 * TODO
 *
 * @author hecvasro
 */
public interface AccountRepository {
  /**
   * TODO
   *
   * @param type
   *   TODO
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
   *
   * @return TODO
   */
  @NonNull
  Observable<Account> create(@AccountType int type, @NonNull String alias, @NonNull String currency,
    @NonNull String bank, double queryFee, @NonNull String queryFeeDescription,
    @NonNull String balanceUrl);

  /**
   * TODO
   *
   * @return TODO
   */
  @NonNull
  Observable<List<Account>> getAll();
}
