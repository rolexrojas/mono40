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
   * TODO
   *
   * @return TODO
   */
  @NonNull
  Observable<ApiResult<List<Bank>>> getAllBanks();

  /**
   * TODO
   *
   * @return TODO
   */
  @NonNull
  Observable<ApiResult<List<Account>>> getInitialData();

  /**
   * TODO
   *
   * @param account
   *   TODO
   * @param pin
   *   TODO
   *
   * @return TODO
   */
  @NonNull
  Observable<ApiResult<Balance>> getAccountBalance(@NonNull Account account, @NonNull String pin);
}
