package com.gbh.movil.domain.api;

import android.support.annotation.NonNull;

import com.gbh.movil.domain.Account;
import com.gbh.movil.domain.Balance;
import com.gbh.movil.domain.Bank;
import com.gbh.movil.domain.InitialData;
import com.gbh.movil.domain.Transaction;

import java.util.List;
import java.util.Set;

import rx.Observable;

/**
 * Contract that defines all the required methods to communicate with the API.
 *
 * @author hecvasro
 */
public interface ApiBridge {
  /**
   * Gets all the available {@link Bank banks} from the API.
   *
   * @return All the available {@link Bank banks} from the API.
   */
  @NonNull
  Observable<ApiResult<Set<Bank>>> getAllBanks();

  /**
   * TODO
   *
   * @return TODO
   */
  @NonNull
  Observable<ApiResult<InitialData>> initialLoad();

  /**
   * Query the {@link Balance balance} of an {@link Account account} from the API.
   *
   * @param account
   *   {@link Account} that will be queried.
   * @param pin
   *   User's PIN.
   *
   * @return {@link Balance balance} of an {@link Account account} from the API.
   */
  @NonNull
  Observable<ApiResult<Balance>> queryBalance(@NonNull Account account, @NonNull String pin);

  /**
   * TODO
   *
   * @return TODO
   */
  @NonNull
  Observable<ApiResult<List<Transaction>>> recentTransactions();
}
