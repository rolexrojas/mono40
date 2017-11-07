package com.tpago.movil.d.domain.pos;

import com.tpago.movil.PhoneNumber;

import rx.Observable;

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

  void unregister(PhoneNumber phoneNumber) throws Exception;
}
