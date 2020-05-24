package com.mono40.movil.session;

import java.security.PublicKey;

import io.reactivex.Single;

/**
 * @author hecvasro
 */
public interface UnlockMethodKeyGenerator {

  UnlockMethod method();

  Single<PublicKey> generate();
}
