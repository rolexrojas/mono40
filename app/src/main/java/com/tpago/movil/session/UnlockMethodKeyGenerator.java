package com.tpago.movil.session;

import java.security.PublicKey;

import io.reactivex.Single;

/**
 * @author hecvasro
 */
public interface UnlockMethodKeyGenerator {

  UnlockMethod method();

  Single<PublicKey> generate();
}
