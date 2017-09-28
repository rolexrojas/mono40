package com.tpago.movil.domain.auth;

import com.tpago.movil.util.Placeholder;
import com.tpago.movil.util.Result;

import io.reactivex.Single;

/**
 * @author hecvasro
 */
public interface AltAuthMethodService {

  Single<Result<Placeholder>> clearPublicKey();

  Single<Result<Placeholder>> setPublicKey(String publicKey);

  Single<Result<Placeholder>> verifySignature(String signature);
}
