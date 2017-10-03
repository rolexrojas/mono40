package com.tpago.movil.data.api;

import com.tpago.movil.domain.auth.alt.AltAuthMethodVerifyData;
import com.tpago.movil.util.Placeholder;
import com.tpago.movil.util.Result;

import java.security.PublicKey;
import java.security.UnrecoverableEntryException;

import io.reactivex.Completable;
import io.reactivex.Single;

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
  public Completable enableAltAuthMethod(PublicKey publicKey) {
    return Completable.error(new UnrecoverableEntryException("not implemented"));
  }

  @Override
  public Single<Result<Placeholder>> verifyAltAuthMethod(
    AltAuthMethodVerifyData data,
    byte[] signedData
  ) {
    return Single.error(new UnrecoverableEntryException("not implemented"));
  }

  @Override
  public Completable disableAltAuthMethod() {
    return Completable.error(new UnrecoverableEntryException("not implemented"));
  }
}
