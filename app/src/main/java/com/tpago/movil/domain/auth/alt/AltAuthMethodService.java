package com.tpago.movil.domain.auth.alt;

import com.tpago.movil.util.Placeholder;
import com.tpago.movil.util.Result;

import java.security.PublicKey;
import java.security.Signature;

import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * Service for {@link AltAuthMethod alternative authentication methods}
 *
 * @author hecvasro
 */
public interface AltAuthMethodService {

  /**
   * @param publicKey
   *   {@link PublicKey Key} that will be used for {@link Signature signAlgName} verification.
   *
   * @throws NullPointerException
   *   If {@code publicKey} is {@code null}.
   */
  Completable enable(PublicKey publicKey);

  Single<Result<Placeholder>> verify(byte[] signature);

  Completable disable();
}
