package com.tpago.movil.domain;

import android.support.annotation.NonNull;

import java.util.List;

import rx.Observable;

/**
 * Contract that defines all the methods required for managing {@link Account accounts} locally.
 *
 * @author hecvasro
 */
public interface AccountRepository {
  /**
   * Creates an {@link Account} and stores it locally.
   *
   * @param type
   *   {@link Account}'s type.
   * @param alias
   *   {@link Account}'s identifier.
   * @param currency
   *   {@link Account}'s currency.
   * @param bank
   *   {@link Account}'s {@link Balance holder}.
   * @param queryFee
   *   Cost of querying the balance.
   * @param queryFeeDescription
   *   Description of the cost of querying the balance.
   * @param queryBalanceUrl
   *   Url for querying the balance.
   *
   * @return {@link Account} created and stored locally.
   */
  @NonNull
  Observable<Account> create(@AccountType int type, @NonNull String alias, @NonNull String currency,
    @NonNull String bank, double queryFee, @NonNull String queryFeeDescription,
    @NonNull String queryBalanceUrl);

  /**
   * Gets all the registered {@link Account accounts} stored locally.
   *
   * @return All the registered {@link Account accounts} stored locally.
   */
  @NonNull
  Observable<List<Account>> getAll();
}
