package com.tpago.movil.session;

import android.support.annotation.Nullable;

import com.tpago.movil.util.StringHelper;

import java.util.concurrent.atomic.AtomicReference;

/**
 * In-memory store for access tokens.
 *
 * @author hecvasro
 */
public class AccessTokenStore {

  static AccessTokenStore create() {
    return new AccessTokenStore();
  }

  private final AtomicReference<String> reference;

  private AccessTokenStore() {
    this.reference = new AtomicReference<>();
  }

  public final void set(String accessToken) {
    this.reference.set(StringHelper.nullIfEmpty(accessToken));
  }

  public final boolean isSet() {
    return !StringHelper.isNullOrEmpty(this.reference.get());
  }

  @Nullable
  public final String get() {
    return this.reference.get();
  }

  public final void clear() {
    this.reference.set(null);
  }
}
