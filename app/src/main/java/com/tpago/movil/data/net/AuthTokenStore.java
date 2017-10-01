package com.tpago.movil.data.net;

import android.support.annotation.Nullable;

import com.tpago.movil.util.ObjectHelper;

import java.util.concurrent.atomic.AtomicReference;

/**
 * In-memory store for {@link AuthToken authorization tokens}.
 *
 * @author hecvasro
 */
class AuthTokenStore {

  static AuthTokenStore create() {
    return new AuthTokenStore();
  }

  private final AtomicReference<AuthToken> reference;

  private AuthTokenStore() {
    this.reference = new AtomicReference<>();
  }

  final void clear() {
    this.reference.set(null);
  }

  final void set(AuthToken authToken) {
    this.reference.set(ObjectHelper.checkNotNull(authToken, "authToken"));
  }

  @Nullable
  final AuthToken get() {
    return this.reference.get();
  }
}
