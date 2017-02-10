package com.tpago.movil.d.domain;

import android.support.annotation.NonNull;

import com.tpago.movil.d.domain.api.ApiBridge;
import com.tpago.movil.d.domain.api.ApiCode;
import com.tpago.movil.d.domain.api.ApiUtils;
import com.tpago.movil.d.domain.session.SessionManager;

import java.math.BigDecimal;

import rx.Observable;
import rx.functions.Func1;

/**
 * TODO
 *
 * @author hecvasro
 */
public final class TransactionManager {
  private final ApiBridge apiBridge;
  private final com.tpago.movil.d.domain.session.SessionManager sessionManager;

  public TransactionManager(@NonNull ApiBridge apiBridge, @NonNull SessionManager sessionManager) {
    this.apiBridge = apiBridge;
    this.sessionManager = sessionManager;
  }

  /**
   * TODO
   *
   * @param recipient
   *   TODO
   * @param amount
   *   TODO
   * @param pin
   *   TODO
   *
   * @return TODO
   */
  @NonNull
  public final Observable<Boolean> transferTo(@NonNull Product product,
    @NonNull Recipient recipient, @NonNull BigDecimal amount, @NonNull String pin) {
    return apiBridge.transferTo(sessionManager.getSession().getAuthToken(), product, recipient,
      amount, pin)
      .compose(ApiUtils.handleApiResult(true, new Func1<ApiCode, Observable<Boolean>>() {
        @Override
        public Observable<Boolean> call(ApiCode code) {
          return Observable.just(false);
        }
      }));
  }
}
