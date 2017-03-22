package com.tpago.movil.d.domain.recipient;

import android.support.annotation.NonNull;

import com.tpago.movil.d.domain.PhoneNumber;
import com.tpago.movil.d.domain.api.ApiResult;

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
