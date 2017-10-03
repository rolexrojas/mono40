package com.tpago.movil.data.api;

import android.support.annotation.IntDef;

import com.tpago.movil.domain.auth.alt.AltAuthMethodVerifyData;
import com.tpago.movil.util.Placeholder;
import com.tpago.movil.util.Result;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.security.PublicKey;

import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * @author hecvasro
 */
public interface Api {

  Completable enableAltAuthMethod(PublicKey publicKey);

  Single<Result<Placeholder>> verifyAltAuthMethod(AltAuthMethodVerifyData data, byte[] signedData);

  Completable disableAltAuthMethod();

  @IntDef({
    FailureCode.UNEXPECTED
  })
  @Retention(RetentionPolicy.SOURCE)
  @interface FailureCode {

    int UNAUTHORIZED = 401;
    int UNEXPECTED = 500;
  }
}
