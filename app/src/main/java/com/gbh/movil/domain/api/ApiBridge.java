package com.gbh.movil.domain.api;

import android.support.annotation.NonNull;

import com.gbh.movil.domain.Account;
import com.gbh.movil.domain.Balance;
import com.gbh.movil.domain.Bank;
import com.gbh.movil.domain.InitialData;
import com.gbh.movil.domain.Result;
import com.gbh.movil.domain.Transaction;

import java.util.List;
import java.util.Set;

import rx.Observable;

/**
 * TODO
 *
 * @author hecvasro
 */
public interface ApiBridge {
  /**
   * Creates an {@link Observable observable} that emits all the associated {@link Bank banks}.
   *
   * @return An {@link Observable observable} that emits all the associated {@link Bank banks}.
   */
  @NonNull
  Observable<Result<ApiCode, Set<Bank>>> banks();

  /**
   * TODO
   *
   * @return TODO
   */
  @NonNull
  Observable<Result<ApiCode, InitialData>> initialLoad();

  /**
   * Creates an {@link Observable observable} that emits all the registered {@link Account
   * accounts}.
   *
   * @return An {@link Observable observable} that emits all the registered {@link Account
   * accounts}.
   */
  @NonNull
  Observable<Result<ApiCode, Set<Account>>> accounts();

  /**
   * Query the {@link Balance balance} of an {@link Account account} from the API.
   *
   * @param account
   *   {@link Account} that will be queried.
   * @param pin
   *   User's PIN code.
   *
   * @return {@link Balance balance} of an {@link Account account} from the API.
   */
  @NonNull
  Observable<Result<ApiCode, Balance>> queryBalance(@NonNull Account account, @NonNull String pin);

  /**
   * Creates an {@link Observable observable} that emits the last transactions made.
   *
   * @return An {@link Observable observable} that emits the last transactions made.
   */
  @NonNull
  Observable<Result<ApiCode, List<Transaction>>> recentTransactions();
}
