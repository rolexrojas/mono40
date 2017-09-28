package com.tpago.movil.domain.auth;

import com.tpago.movil.util.Placeholder;
import com.tpago.movil.util.Result;

import java.security.PublicKey;

import io.reactivex.Single;

/**
 * @author hecvasro
 */
public interface AltUnlockMethodService {

  Single<Result<Placeholder>> clearPublicKey();

  Single<Result<Placeholder>> setPublicKey(PublicKey publicKey);

  Single<Result<Placeholder>> verifySignature(byte[] signature);
}
