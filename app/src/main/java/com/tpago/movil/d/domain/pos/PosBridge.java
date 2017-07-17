package com.tpago.movil.d.domain.pos;

import rx.Observable;
import rx.Single;

/**
 * @author hecvasro
 */
@Deprecated
public interface PosBridge {
  boolean checkIfUsable();

  boolean isRegistered(String identifier);
  Observable<PosResult> selectCard(String identifier);

  PosResult addCard(String phoneNumber, String pin, String identifier);
  PosResult removeCard(String identifier);

  Single<PosResult> unregister(String phoneNumber);
}
