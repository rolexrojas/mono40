package com.tpago.movil.data.api;

import com.tpago.movil.domain.Email;
import com.tpago.movil.domain.PhoneNumber;
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

  private final User user;

  private AtomicReference<String> altAuthPublicKey = new AtomicReference<>();

  static MockApi create() {
    return new MockApi();
  }

  private MockApi() {
    this.user = User.builder()
      .id(1)
      .phoneNumber(PhoneNumber.create("8098829887"))
      .email(Email.create("hecvasro@gmail.com"))
      .firstName("Hector")
      .lastName("Vasquez")
      .picture("https://www.gravatar.com/avatar/b2adb8d6dcefd53ceb978fc4a3018ad0")
      .build();
  }

  @Override
  public Single<Response<User>> signIn(@Body ApiSignInBody body) {
    return Single.just(Response.success(this.user))
      .delay(1L, TimeUnit.SECONDS);
  }

  @Override
  public Single<Placeholder> enableAltAuth(@Body ApiEnableAltAuthBody body) {
    return Single.just(Placeholder.get())
      .delay(1L, TimeUnit.SECONDS)
      .doOnSuccess((v) -> this.altAuthPublicKey.set(body.publicKey()));
  }

  @Override
  public Single<Response<User>> verifySignedData(@Body ApiVerifyAltAuthBody body) {
    return Single.just(Response.success(this.user))
      .delay(1L, TimeUnit.SECONDS);
  }

  @Override
  public Single<Placeholder> disableAltAuth() {
    return Single.just(Placeholder.get())
      .delay(1L, TimeUnit.SECONDS)
      .doOnSuccess((v) -> this.altAuthPublicKey.set(null));
  }
}
