package com.tpago.movil.d.domain.session;

import android.support.annotation.NonNull;

import com.tpago.movil.d.domain.api.ApiResult;

import rx.Observable;

/**
 * TODO
 *
 * @author hecvasro
 */
@Deprecated
public interface SessionService {
  /**
   * TODO
   *
   * @param phoneNumber
   *   TODO
   * @param email
   *   TODO
   * @param password
   *   TODO
   * @param deviceId
   *   TODO
   *
   * @return TODO
   */
  @NonNull
  Observable<ApiResult<String>> signIn(@NonNull String phoneNumber, @NonNull String email,
    @NonNull String password, @NonNull String deviceId, boolean force);

  /**
   * TODO
   *
   * @param phoneNumber
   *   TODO
   * @param email
   *   TODO
   * @param password
   *   TODO
   * @param deviceId
   *   TODO
   * @param pin
   *   TODO
   *
   * @return TODO
   */
  @NonNull
  Observable<ApiResult<String>> signUp(@NonNull String phoneNumber, @NonNull String email,
    @NonNull String password, @NonNull String deviceId, @NonNull String pin);
}
