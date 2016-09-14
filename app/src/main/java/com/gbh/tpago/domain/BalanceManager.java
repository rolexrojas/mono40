package com.gbh.tpago.domain;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gbh.tpago.domain.api.ApiBridge;
import com.gbh.tpago.domain.api.ApiResult;

import rx.Observable;

/**
 * TODO
 *
 * @author hecvasro
 */
public final class BalanceManager {
  /**
   * TODO
   */
  private final ApiBridge apiBridge;

  /**
   * TODO
   *
   * @param apiBridge
   *   TODO
   */
  public BalanceManager(@NonNull ApiBridge apiBridge) {
    this.apiBridge = apiBridge;
  }

  /**
   * TODO
   */
  public final void start() {
    // TODO
  }

  /**
   * TODO
   */
  public final void stop() {
    // TODO
  }

  /**
   * TODO
   *
   * @return TODO
   */
  public final Observable<Account> expiration() {
    // TODO
    return Observable.error(new UnsupportedOperationException());
  }

  /**
   * TODO
   *
   * @param account
   *   TODO
   *
   * @return TODO
   */
  public final boolean hasValidBalance(@NonNull Account account) {
    // TODO
    return false;
  }

  /**
   * TODO
   *
   * @param account
   *   TODO
   *
   * @return TODO
   */
  @Nullable
  public final Balance getBalance(@NonNull Account account) {
    // TODO
    return null;
  }

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
  public final Observable<ApiResult<Balance>> requestBalance(@NonNull Account account,
    @NonNull String pin) {
    // TODO
    return Observable.error(new UnsupportedOperationException());
  }
}
