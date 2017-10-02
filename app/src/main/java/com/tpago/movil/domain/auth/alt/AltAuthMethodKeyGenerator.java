package com.tpago.movil.domain.auth.alt;

import java.security.PublicKey;

import io.reactivex.Single;

/**
 * @author hecvasro
 */
public interface AltAuthMethodKeyGenerator {

  AltAuthMethod method();

  Single<PublicKey> generate();
}
