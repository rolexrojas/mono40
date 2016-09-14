package com.gbh.tpago.domain;

import android.support.annotation.NonNull;

import com.gbh.tpago.domain.api.ApiBridge;
import com.gbh.tpago.domain.api.ApiResult;

import rx.Observable;

/**
 * TODO
 *
 * @author hecvasro
 */
public final class InitialDataLoader {
  /**
   * TODO
   */
  private final ApiBridge apiBridge;

  /**
   * TODO
   */
  private final AccountRepository accountRepository;

  /**
   * TODO
   */
  private final BankRepository bankRepository;

  /**
   * TODO
   *
   * @param apiBridge
   *   TODO
   * @param accountRepository
   *   TODO
   * @param bankRepository
   *   TODO
   */
  public InitialDataLoader(@NonNull ApiBridge apiBridge,
    @NonNull AccountRepository accountRepository, @NonNull BankRepository bankRepository) {
    this.apiBridge = apiBridge;
    this.accountRepository = accountRepository;
    this.bankRepository = bankRepository;
  }

  /**
   * TODO
   *
   * @return TODO
   */
  @NonNull
  public final Observable<ApiResult<InitialData>> load() {
    // TODO
    return Observable.error(new UnsupportedOperationException());
  }
}
