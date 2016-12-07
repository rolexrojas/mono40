package com.gbh.movil.domain.recipient;

import android.support.annotation.NonNull;

import com.gbh.movil.domain.PhoneNumber;
import com.gbh.movil.domain.api.ApiCode;
import com.gbh.movil.misc.Result;

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
  Observable<Result<ApiCode, String>> getName(@NonNull String authToken,
    @NonNull PhoneNumber phoneNumber);
}
