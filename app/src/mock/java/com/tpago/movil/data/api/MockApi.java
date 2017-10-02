package com.tpago.movil.data.api;

import com.tpago.movil.domain.user.User;
import com.tpago.movil.util.Placeholder;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.Body;

/**
 * @author hecvasro
 */
final class MockApi implements Api {

  private AtomicReference<String> altAuthPublicKey = new AtomicReference<>();

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
  public Single<Placeholder> enableAltAuth(@Body ApiEnableAltAuthBody body) {
    return Single.just(Placeholder.get())
      .delay(1L, TimeUnit.SECONDS)
      .doOnSuccess((v) -> this.altAuthPublicKey.set(body.publicKey()));
  }

  @Override
  public Single<Placeholder> disableAltAuth() {
    return Single.just(Placeholder.get())
      .delay(1L, TimeUnit.SECONDS)
      .doOnSuccess((v) -> this.altAuthPublicKey.set(null));
  }
}
