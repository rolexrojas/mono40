package com.tpago.movil.data.auth.alt;

import com.tpago.movil.data.api.Api;
import com.tpago.movil.data.api.ApiEnableAltAuthBody;
import com.tpago.movil.domain.auth.alt.AltAuthService;
import com.tpago.movil.util.ObjectHelper;
import com.tpago.movil.util.Placeholder;
import com.tpago.movil.util.Result;

import java.security.PublicKey;

import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * @author hecvasro
 */
final class ApiAltAuthService implements AltAuthService {

  public static ApiAltAuthService create(Api api) {
    return new ApiAltAuthService(api);
  }

  private final Api api;

  private ApiAltAuthService(Api api) {
    this.api = ObjectHelper.checkNotNull(api, "api");
  }

  @Override
  public Completable enable(PublicKey publicKey) {
    final ApiEnableAltAuthBody body = ApiEnableAltAuthBody.builder()
      .publicKey(publicKey)
      .build();
    return this.api.enableAltAuth(body)
      .toCompletable();
  }

  @Override
  public Single<Result<Placeholder>> verify(byte[] signature) {
    return Single.error(new UnsupportedOperationException("not implemented"));
  }

  @Override
  public Completable disable() {
    return this.api.disableAltAuth()
      .toCompletable();
  }
}
