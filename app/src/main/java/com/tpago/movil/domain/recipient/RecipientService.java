package com.tpago.movil.domain.recipient;

import android.support.annotation.NonNull;

import com.tpago.movil.domain.PhoneNumber;
import com.tpago.movil.domain.api.ApiResult;

import rx.Observable;

/**
 * TODO
 *
 * @author hecvasro
 */
public interface RecipientService {
  /**
   * TODO
   *
   * @param authToken
   *   TODO
   * @param phoneNumber
   *   TODO
   *
   * @return TODO
   */
  Observable<ApiResult<String>> getName(@NonNull String authToken,
    @NonNull PhoneNumber phoneNumber);
}
