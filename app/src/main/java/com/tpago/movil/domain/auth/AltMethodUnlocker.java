package com.tpago.movil.domain.auth;

import com.tpago.movil.domain.user.User;
import com.tpago.movil.util.Placeholder;
import com.tpago.movil.util.Result;

import java.security.PrivateKey;

import io.reactivex.Single;

/**
 * @author hecvasro
 */
public final class AltMethodUnlocker implements Unlocker {

  private final User user;
  private final String deviceId;
  private final PrivateKey privateKey;
  private final AltUnlockMethodService altUnlockMethodService;

  private AltMethodUnlocker() {
    throw new UnsupportedOperationException("not implemented");
  }

  @Override
  public Single<Result<Placeholder>> unlock() {
    throw new UnsupportedOperationException("not implemented");
  }
}
