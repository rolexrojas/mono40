package com.tpago.movil.domain.pos;

import rx.Observable;

/**
 * @author hecvasro
 */
public interface PosBridge {
  Observable<PosResult<String>> addCard(String phoneNumber, String pin, String alias);

  Observable<PosResult<String>> selectCard(String alias);

  Observable<PosResult<String>> removeCard(String alias);

  Observable<PosResult<String>> reset(String phoneNumber);

  boolean isActive(String alias);
}
