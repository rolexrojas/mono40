package com.tpago.movil.dep.domain.recipient;

import android.support.annotation.NonNull;

import com.tpago.movil.dep.domain.PhoneNumber;
import com.tpago.movil.dep.domain.api.ApiResult;

import rx.Observable;

/**
 * TODO
 *
 * @author hecvasro
 */
public interface RecipientService {
  Observable<ApiResult<String>> getName(@NonNull String authToken,
    @NonNull PhoneNumber phoneNumber);
}
