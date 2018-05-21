package com.tpago.movil.d.domain.pos;

import com.tpago.movil.PhoneNumber;

import rx.Single;

@Deprecated
public interface PosBridge {

  boolean isAvailable();

  boolean isRegistered(String identifier);

  PosResult addCard(String phoneNumber, String pin, String identifier);

  PosResult removeCard(String identifier);

  void unregister(PhoneNumber phoneNumber) throws Exception;

  Single<PosResult> selectCard(String identifier);
}
