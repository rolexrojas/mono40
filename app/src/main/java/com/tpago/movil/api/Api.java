package com.tpago.movil.api;

import android.net.Uri;
import android.support.annotation.IntDef;

import com.tpago.movil.Code;
import com.tpago.movil.Email;
import com.tpago.movil.Password;
import com.tpago.movil.PhoneNumber;
import com.tpago.movil.domain.auth.alt.AltOpenSessionSignatureData;
import com.tpago.movil.payment.Carrier;
import com.tpago.movil.session.User;
import com.tpago.movil.util.Placeholder;
import com.tpago.movil.util.Result;

import java.io.File;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.security.PublicKey;

import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * @author hecvasro
 */
public interface Api {

  Single<Result<User>> createSession(
    PhoneNumber phoneNumber,
    Email email,
    String firstName,
    String lastName,
    Password password,
    Code pin,
    String deviceId
  );

  Single<Result<User>> createSession(
    PhoneNumber phoneNumber,
    Email email,
    Password password,
    String deviceId,
    boolean shouldDeactivatePreviousDevice
  );

  Result<Placeholder> updateUserName(User user, String firstName, String lastName);

  Result<Uri> updateUserPicture(User user, File picture);

  Result<Placeholder> updateUserCarrier(User user, Carrier carrier);

  Completable enableAltOpenSessionMethod(PublicKey publicKey);

  Single<Result<Placeholder>> openSession(AltOpenSessionSignatureData signatureData, byte[] signedData);

  Completable disableAltOpenSessionMethod();

  @IntDef({
    FailureCode.ALREADY_ASSOCIATED_DEVICE,
    FailureCode.ALREADY_ASSOCIATED_PROFILE,
    FailureCode.ALREADY_REGISTERED_EMAIL,
    FailureCode.ALREADY_REGISTERED_USERNAME,
    FailureCode.INCORRECT_USERNAME_AND_PASSWORD,
    FailureCode.INVALID_DEVICE_ID,
    FailureCode.INVALID_EMAIL,
    FailureCode.INVALID_PASSWORD,
    FailureCode.INVALID_PHONE_NUMBER,
    FailureCode.INVALID_PIN,
    FailureCode.UNASSOCIATED_PHONE_NUMBER,
    FailureCode.UNASSOCIATED_PROFILE,
    FailureCode.UNAVAILABLE_SERVICE,
    FailureCode.INCORRECT_CODE
  })
  @Retention(RetentionPolicy.SOURCE)
  @interface FailureCode {

    int ALREADY_ASSOCIATED_DEVICE = 112;
    int ALREADY_ASSOCIATED_PROFILE = 12;
    int ALREADY_REGISTERED_EMAIL = 55;
    int ALREADY_REGISTERED_USERNAME = 21;
    int INCORRECT_USERNAME_AND_PASSWORD = 4;
    int INVALID_DEVICE_ID = 9;
    int INVALID_EMAIL = 0;
    int INVALID_PASSWORD = 1618;
    int INVALID_PHONE_NUMBER = 13;
    int INVALID_PIN = 16;
    int UNASSOCIATED_PHONE_NUMBER = 216;
    int UNASSOCIATED_PROFILE = 144;
    int UNAVAILABLE_SERVICE = 14;

    int INCORRECT_CODE = 7000;

    int UNAUTHORIZED = 10401;
    int UNEXPECTED = 10500;
  }
}
