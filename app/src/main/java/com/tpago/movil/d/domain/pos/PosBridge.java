package com.tpago.movil.d.domain.pos;

import rx.Observable;
import rx.Single;

/**
 * @author hecvasro
 */
@Deprecated
public interface PosBridge {
  boolean isRegistered(String alias);
  Observable<PosResult> selectCard(String alias);

  PosResult addCard(String phoneNumber, String pin, String alias);
  PosResult removeCard(String alias);

  Single<PosResult> unregister(String phoneNumber);
}
