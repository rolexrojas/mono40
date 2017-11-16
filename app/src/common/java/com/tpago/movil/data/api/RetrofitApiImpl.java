package com.tpago.movil.data.api;

import android.net.Uri;

import com.tpago.movil.Code;
import com.tpago.movil.Email;
import com.tpago.movil.Name;
import com.tpago.movil.Password;
import com.tpago.movil.PhoneNumber;
import com.tpago.movil.api.Api;
import com.tpago.movil.bank.Bank;
import com.tpago.movil.dep.MimeType;
import com.tpago.movil.partner.Carrier;
import com.tpago.movil.session.UnlockMethodSignatureData;
import com.tpago.movil.session.User;
import com.tpago.movil.util.ObjectHelper;
import com.tpago.movil.util.Result;

import java.io.File;
import java.security.PublicKey;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;

/**
 * @author hecvasro
 */
final class RetrofitApiImpl implements Api {

  static RetrofitApiImpl create(
    RetrofitApi retrofitApi,
    RetrofitApiResultMapper.Builder retrofitApiResultMapperBuilder
  ) {
    return new RetrofitApiImpl(retrofitApi, retrofitApiResultMapperBuilder);
  }

  private final RetrofitApi retrofitApi;
  private final RetrofitApiResultMapper.Builder retrofitApiResultMapperBuilder;

  private RetrofitApiImpl(
    RetrofitApi retrofitApi,
    RetrofitApiResultMapper.Builder retrofitApiResultMapperBuilder
  ) {
    this.retrofitApi = ObjectHelper.checkNotNull(retrofitApi, "retrofitApi");
    this.retrofitApiResultMapperBuilder = ObjectHelper
      .checkNotNull(retrofitApiResultMapperBuilder, "retrofitApiResultMapperBuilder");
  }

  @Override
  public Single<List<Bank>> getBanks() {
    return Single.error(new UnsupportedOperationException("not implemented"));
  }

  @Override
  public Single<Result<User>> createSession(
    PhoneNumber phoneNumber,
    Email email,
    String firstName,
    String lastName,
    Password password,
    Code pin,
    String deviceId
  ) {
    final RetrofitApiSignUpBody body = RetrofitApiSignUpBody.builder()
      .phoneNumber(phoneNumber)
      .email(email)
      .firstName(firstName)
      .lastName(lastName)
      .password(password)
      .pin(pin)
      .deviceId(deviceId)
      .build();
    return this.retrofitApi.signUp(body)
      .map(this.retrofitApiResultMapperBuilder.build());
  }

  @Override
  public Single<Result<User>> createSession(
    PhoneNumber phoneNumber,
    Email email,
    Password password,
    String deviceId,
    boolean shouldDeactivatePreviousDevice
  ) {
    final Single<Response<User>> single;
    if (shouldDeactivatePreviousDevice) {
      final RetrofitApiAssociateDeviceBody body = RetrofitApiAssociateDeviceBody.builder()
        .phoneNumber(phoneNumber)
        .email(email)
        .password(password)
        .deviceId(deviceId)
        .build();
      single = this.retrofitApi.associateDevice(body);
    } else {
      final RetrofitApiSignInBody body = RetrofitApiSignInBody.builder()
        .phoneNumber(phoneNumber)
        .email(email)
        .password(password)
        .deviceId(deviceId)
        .build();
      single = this.retrofitApi.signIn(body);
    }
    return single.map(this.retrofitApiResultMapperBuilder.build());
  }

  @Override
  public Completable updateUserName(User user, Name name) {
    ObjectHelper.checkNotNull(user, "user");
    ObjectHelper.checkNotNull(name, "name");
    return this.retrofitApi.updateUserName(ApiName.create(name))
      .concatWith(this.retrofitApi.updateBeneficiary(ApiBeneficiary.create(user, name)));
  }

  @Override
  public Single<Uri> updateUserPicture(User user, File picture) {
    ObjectHelper.checkNotNull(user, "user");
    ObjectHelper.checkNotNull(picture, "picture");
    final RequestBody requestPicture = RequestBody
      .create(MediaType.parse(MimeType.IMAGE), picture);
    final MultipartBody.Part body = MultipartBody.Part
      .createFormData("file", picture.getName(), requestPicture);
    return this.retrofitApi.updateUserPicture(body)
      .map(User::picture);
  }

  @Override
  public Completable updateUserCarrier(User user, Carrier carrier) {
    ObjectHelper.checkNotNull(user, "user");
    ObjectHelper.checkNotNull(carrier, "carrier");
    return this.retrofitApi.updateBeneficiary(ApiBeneficiary.create(user, carrier));
  }

  @Override
  public Completable enableUnlockMethod(PublicKey publicKey) {
    final RetrofitApiEnableSessionOpeningBody body = RetrofitApiEnableSessionOpeningBody.builder()
      .publicKey(publicKey)
      .build();
    return this.retrofitApi.enableSessionOpeningMethod(body);
  }

  @Override
  public Single<Result<User>> openSession(
    UnlockMethodSignatureData signatureData,
    byte[] signedData
  ) {
    final RetrofitApiOpenSessionBody body = RetrofitApiOpenSessionBody.builder()
      .user(signatureData.user())
      .deviceId(signatureData.deviceId())
      .signedData(signedData)
      .build();
    return this.retrofitApi.openSession(body)
      .map(this.retrofitApiResultMapperBuilder.build());
  }

  @Override
  public Completable disableUnlockMethod() {
    return this.retrofitApi.disableSessionOpeningMethod();
  }
}
