package com.mono40.movil.d.data.pos;

import com.mono40.movil.PhoneNumber;
import com.mono40.movil.d.domain.pos.PosBridge;
import com.mono40.movil.d.domain.pos.PosResult;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import rx.Single;

/**
 * @author hecvasro
 */
final class MockPosBridge implements PosBridge {

  static MockPosBridge create() {
    return new MockPosBridge();
  }

  private static final String DATA = UUID.randomUUID()
    .toString();

  private MockPosBridge() {
  }

  @Override
  public boolean isAvailable() {
    return false;
  }

  @Override
  public boolean isRegistered(String identifier) {
    return true;
  }

  @Override
  public PosResult addCard(String phoneNumber, String pin, String identifier) {
    return PosResult.create(DATA);
  }

  @Override
  public PosResult removeCard(String identifier) {
    return PosResult.create(DATA);
  }

  @Override
  public void unregister(PhoneNumber phoneNumber) throws Exception {
  }

  @Override
  public Single<PosResult> selectCard(String identifier) {
    return Single.defer(() -> Single.just(PosResult.create(DATA)))
      .delay(1L, TimeUnit.SECONDS);
  }
}
