package com.tpago.movil.domain.auth;

import android.support.annotation.Nullable;

import com.tpago.movil.util.ObjectHelper;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @author hecvasro
 */
public class AuthorizationTokenStore {

  static AuthorizationTokenStore create() {
    return new AuthorizationTokenStore();
  }

  private final AtomicReference<AuthorizationToken> reference;

  private AuthorizationTokenStore() {
    this.reference = new AtomicReference<>();
  }

  public final void clear() {
    this.reference.set(null);
  }

  public final void set(AuthorizationToken authorizationToken) {
    this.reference.set(ObjectHelper.checkNotNull(authorizationToken, "authorizationToken"));
  }

  @Nullable
  public final AuthorizationToken get() {
    return this.reference.get();
  }
}
