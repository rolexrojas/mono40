package com.tpago.movil.data.auth.alt;

import com.tpago.movil.util.Result;

import java.security.KeyPair;

import io.reactivex.Single;

/**
 * @author hecvasro
 */
public final class CodeAuthMethodMethodKeyPairSupplier implements AltAuthMethodKeyPairSupplier {

  @Override
  public Single<Result<KeyPair>> get() {
    return Single.error(new UnsupportedOperationException("not implemented"));
  }
}
