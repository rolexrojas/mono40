package com.tpago.movil.dep.domain.pos;

import rx.Observable;

/**
 * @author hecvasro
 */
@Deprecated
public interface PosBridge {
  boolean isRegistered(String alias);

  Observable<PosResult> addCard(String phoneNumber, String pin, String alias);

  Observable<PosResult> selectCard(String alias);

  Observable<PosResult> removeCard(String alias);

  Observable<PosResult> reset(String phoneNumber);
}
