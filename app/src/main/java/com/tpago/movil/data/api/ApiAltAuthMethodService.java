package com.tpago.movil.data.api;

import com.tpago.movil.domain.auth.AltAuthMethodService;
import com.tpago.movil.util.Placeholder;
import com.tpago.movil.util.Result;

import io.reactivex.Single;

/**
 * @author hecvasro
 */
final class ApiAltAuthMethodService implements AltAuthMethodService {

  @Override
  public Single<Result<Placeholder>> clearPublicKey() {
    return null;
  }

  @Override
  public Single<Result<Placeholder>> setPublicKey(String publicKey) {
    return null;
  }

  @Override
  public Single<Result<Placeholder>> verifySignature(String signature) {
    return null;
  }
}
