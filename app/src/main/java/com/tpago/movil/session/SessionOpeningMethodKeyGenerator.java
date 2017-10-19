package com.tpago.movil.session;

import java.security.PublicKey;

import io.reactivex.Single;

/**
 * @author hecvasro
 */
public interface SessionOpeningMethodKeyGenerator {

  SessionOpeningMethod method();

  Single<PublicKey> generate();

  void rollback() throws Exception;
}
