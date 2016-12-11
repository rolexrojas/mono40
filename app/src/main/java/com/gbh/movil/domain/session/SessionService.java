package com.gbh.movil.domain.session;

import android.support.annotation.NonNull;

import com.gbh.movil.domain.PhoneNumber;
import com.gbh.movil.domain.api.ApiResult;

import rx.Observable;

/**
 * TODO
 *
 * @author hecvasro
 */
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
    @NonNull String password, @NonNull String deviceId);

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
