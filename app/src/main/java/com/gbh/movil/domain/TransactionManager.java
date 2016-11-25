package com.gbh.movil.domain;

import android.support.annotation.NonNull;

import com.gbh.movil.domain.api.ApiBridge;
import com.gbh.movil.domain.api.ApiUtils;

import java.math.BigDecimal;

import rx.Observable;

/**
 * TODO
 *
 * @author hecvasro
 */
public final class TransactionManager {
  private final ApiBridge apiBridge;

  public TransactionManager(@NonNull ApiBridge apiBridge) {
    this.apiBridge = apiBridge;
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
    return apiBridge.transferTo(product, recipient, amount, pin)
      .compose(ApiUtils.<Boolean>handleApiResult(true));
  }
}
