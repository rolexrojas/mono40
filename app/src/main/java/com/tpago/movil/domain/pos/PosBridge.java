package com.tpago.movil.domain.pos;

import rx.Observable;

/**
 * @author hecvasro
 */
public interface PosBridge {
  boolean isRegistered(String alias);

  Observable<PosResult> addCard(String phoneNumber, String pin, String alias);

  Observable<PosResult> selectCard(String alias);

  Observable<PosResult> removeCard(String alias);

  Observable<PosResult> reset(String phoneNumber);
}
