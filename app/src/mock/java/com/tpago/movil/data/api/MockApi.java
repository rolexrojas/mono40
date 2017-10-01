package com.tpago.movil.data.api;

import com.tpago.movil.domain.user.User;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.Body;

/**
 * @author hecvasro
 */
final class MockApi implements Api {

  static MockApi create() {
    return new MockApi();
  }

  private MockApi() {
  }

  @Override
  public Single<Response<User>> signIn(@Body ApiSignInBody body) {
    return Single.error(new UnsupportedOperationException("not implemented"));
  }

  @Override
  public Single<Void> enableAltAuth(@Body ApiEnableAltAuthBody body) {
    return Single.error(new UnsupportedOperationException("not implemented"));
  }

  @Override
  public Single<Void> disableAltAuth() {
    return Single.error(new UnsupportedOperationException("not implemented"));
  }
}
