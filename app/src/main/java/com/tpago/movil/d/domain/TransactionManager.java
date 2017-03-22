package com.tpago.movil.d.domain;

import android.support.annotation.NonNull;

import com.tpago.movil.d.domain.api.ApiResult;
import com.tpago.movil.d.domain.api.DepApiBridge;
import com.tpago.movil.d.domain.session.SessionManager;

import java.math.BigDecimal;

import rx.Observable;

/**
 * TODO
 *
 * @author hecvasro
 */
@Deprecated
public final class TransactionManager {
  private final DepApiBridge apiBridge;
  private final com.tpago.movil.d.domain.session.SessionManager sessionManager;

  public TransactionManager(@NonNull DepApiBridge apiBridge, @NonNull SessionManager sessionManager) {
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
  public final Observable<ApiResult<String>> transferTo(
    @NonNull Product product,
    @NonNull Recipient recipient,
    @NonNull BigDecimal amount,
    @NonNull String pin) {
    return apiBridge.transferTo(
      sessionManager.getSession().getAuthToken(),
      product,
      recipient,
      amount,
      pin);
  }
}
