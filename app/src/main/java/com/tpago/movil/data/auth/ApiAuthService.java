package com.tpago.movil.data.auth;

import com.tpago.movil.data.api.Api;
import com.tpago.movil.data.api.ApiSignInBody;
import com.tpago.movil.domain.Email;
import com.tpago.movil.domain.Password;
import com.tpago.movil.domain.PhoneNumber;
import com.tpago.movil.domain.auth.AuthService;
import com.tpago.movil.domain.user.User;
import com.tpago.movil.util.ObjectHelper;
import com.tpago.movil.util.Result;

import io.reactivex.Single;

/**
 * @author hecvasro
 */
final class ApiAuthService implements AuthService {

  static ApiAuthService create(Api api) {
    return new ApiAuthService(api);
  }

  private final Api api;

  private ApiAuthService(Api api) {
    this.api = ObjectHelper.checkNotNull(api, "api");
  }

  @Override
  public Single<Result<User>> signIn(
    PhoneNumber phoneNumber,
    Email email,
    Password password,
    String deviceId,
    boolean shouldDeactivatePreviousDevice
  ) {
    final ApiSignInBody apiSignInBody = ApiSignInBody.builder()
      .phoneNumber(phoneNumber)
      .email(email)
      .password(password)
      .deviceId(deviceId)
      .build();

    throw new UnsupportedOperationException("not implemented");
  }
}
