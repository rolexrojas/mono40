package com.tpago.movil.data.api;

import com.tpago.movil.Code;
import com.tpago.movil.Email;
import com.tpago.movil.Password;
import com.tpago.movil.PhoneNumber;
import com.tpago.movil.api.Api;
import com.tpago.movil.domain.auth.alt.AltAuthMethodVerifyData;
import com.tpago.movil.user.User;
import com.tpago.movil.util.ObjectHelper;
import com.tpago.movil.util.Placeholder;
import com.tpago.movil.util.Result;

import java.security.PublicKey;

import io.reactivex.Completable;
import io.reactivex.Single;
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
  public Single<Result<User>> signUp(
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
  public Single<Result<User>> signIn(
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
  public Completable enableAltAuthMethod(PublicKey publicKey) {
    final RetrofitApiEnableAltAuthBody body = RetrofitApiEnableAltAuthBody.builder()
      .publicKey(publicKey)
      .build();
    return this.retrofitApi.enableAltAuthMethod(body);
  }

  @Override
  public Single<Result<Placeholder>> verifyAltAuthMethod(
    AltAuthMethodVerifyData data,
    byte[] signedData
  ) {
    final RetrofitApiVerifyAltAuthBody body = RetrofitApiVerifyAltAuthBody.builder()
      .user(data.user())
      .deviceId(data.deviceId())
      .signedData(signedData)
      .build();
    return this.retrofitApi.verifyAltAuthMethod(body)
      .map(this.retrofitApiResultMapperBuilder.build());
  }

  @Override
  public Completable disableAltAuthMethod() {
    return this.retrofitApi.disableAltAuthMethod();
  }
}
