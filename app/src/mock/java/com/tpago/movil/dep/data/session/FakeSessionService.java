package com.tpago.movil.dep.data.session;

import android.support.annotation.NonNull;

import com.tpago.movil.dep.domain.api.ApiCode;
import com.tpago.movil.dep.domain.api.ApiResult;
import com.tpago.movil.dep.domain.session.SessionService;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import rx.Observable;

/**
 * @author hecvasro
 */
final class FakeSessionService implements SessionService {
  @NonNull
  @Override
  public Observable<ApiResult<String>> signIn(
    @NonNull String phoneNumber,
    @NonNull String email,
    @NonNull String password,
    @NonNull String deviceId,
    boolean force) {
    return Observable.just(new ApiResult<>(ApiCode.OK, UUID.randomUUID().toString(), null))
      .delay(1L, TimeUnit.SECONDS);
  }

  @NonNull
  @Override
  public Observable<ApiResult<String>> signUp(
    @NonNull String phoneNumber,
    @NonNull String email,
    @NonNull String password,
    @NonNull String deviceId,
    @NonNull String pin) {
    return Observable.just(new ApiResult<>(ApiCode.OK, UUID.randomUUID().toString(), null))
      .delay(1L, TimeUnit.SECONDS);
  }
}
