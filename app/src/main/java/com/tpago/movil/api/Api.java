package com.tpago.movil.api;

import android.support.annotation.IntDef;

import com.tpago.movil.Code;
import com.tpago.movil.Email;
import com.tpago.movil.Password;
import com.tpago.movil.PhoneNumber;
import com.tpago.movil.domain.auth.alt.AltAuthMethodVerifyData;
import com.tpago.movil.user.User;
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

  Single<Result<User>> signUp(
    PhoneNumber phoneNumber,
    Email email,
    Password password,
    Code pin,
    String deviceId
  );

  Single<Result<User>> signIn(
    PhoneNumber phoneNumber,
    Email email,
    Password password,
    String deviceId,
    boolean shouldDeactivatePreviousDevice
  );

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
