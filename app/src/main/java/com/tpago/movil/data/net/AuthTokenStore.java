package com.tpago.movil.data.net;

import android.support.annotation.Nullable;

import com.tpago.movil.util.ObjectHelper;

import java.util.concurrent.atomic.AtomicReference;

/**
 * In-memory store for {@link AuthToken authorization tokens}.
 *
 * @author hecvasro
 */
public class AuthTokenStore {

  static AuthTokenStore create() {
    return new AuthTokenStore();
  }

  private final AtomicReference<AuthToken> reference;

  private AuthTokenStore() {
    this.reference = new AtomicReference<>();
  }

  public final void clear() {
    this.reference.set(null);
  }

  public final void set(AuthToken authToken) {
    this.reference.set(ObjectHelper.checkNotNull(authToken, "authToken"));
  }

  @Nullable
  public final AuthToken get() {
    return this.reference.get();
  }
}
