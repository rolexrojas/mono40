package com.tpago.movil.domain.api;

import android.support.annotation.NonNull;

import com.tpago.movil.domain.Account;
import com.tpago.movil.domain.Balance;
import com.tpago.movil.domain.Bank;

import java.util.List;

import rx.Observable;

/**
 * TODO
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
  Observable<ApiResult<List<Bank>>> getAllBanks();

  /**
   * Gets all the registered {@link Account accounts} from the API.
   *
   * @return All the registered {@link Account accounts} from the API.
   */
  @NonNull
  Observable<ApiResult<List<Account>>> getInitialData();

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
  Observable<ApiResult<Balance>> queryAccountBalance(@NonNull Account account, @NonNull String pin);
}
