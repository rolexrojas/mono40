package com.tpago.movil.d.data.pos;

import com.tpago.movil.PhoneNumber;
import com.tpago.movil.d.domain.pos.PosBridge;
import com.tpago.movil.d.domain.pos.PosCode;
import com.tpago.movil.d.domain.pos.PosResult;
import com.tpago.movil.util.ObjectHelper;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Single;

final class MockPosBridge implements PosBridge {

  static MockPosBridge create() {
    return new MockPosBridge();
  }

  private final Set<String> identifierSet = new HashSet<>();

  private MockPosBridge() {
  }

  @Override
  public boolean checkIfUsable() {
    return true;
  }

  @Override
  public boolean isRegistered(String identifier) {
    return this.identifierSet.contains(identifier);
  }

  @Override
  public Observable<PosResult> selectCard(String identifier) {
    return Observable.just(new PosResult(PosCode.OK, identifier))
      .delay(1L, TimeUnit.SECONDS);
  }

  @Override
  public PosResult addCard(String phoneNumber, String pin, String identifier) {
    if (!this.identifierSet.contains(identifier)) {
      synchronized (this) {
        this.identifierSet.add(identifier);
      }
    }
    return new PosResult(PosCode.OK, identifier);
  }

  @Override
  public PosResult removeCard(String identifier) {
    if (this.identifierSet.contains(identifier)) {
      synchronized (this) {
        this.identifierSet.remove(identifier);
      }
    }
    return new PosResult(PosCode.OK, identifier);
  }

  @Override
  public void unregister(PhoneNumber phoneNumber) throws Exception {
    synchronized (this) {
      this.identifierSet.clear();
    }
    final Throwable throwable = Single.just(new PosResult(PosCode.OK, phoneNumber.value()))
      .delay(1L, TimeUnit.SECONDS)
      .toCompletable()
      .get();
    if (ObjectHelper.isNotNull(throwable)) {
      throw new Exception(throwable);
    }
  }
}
