package com.tpago.movil.session;

import android.support.annotation.Nullable;

import com.tpago.movil.util.StringHelper;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @author hecvasro
 */
public class AccessTokenManager {

  static AccessTokenManager create() {
    return new AccessTokenManager();
  }

  private final AtomicReference<String> reference;

  private AccessTokenManager() {
    this.reference = new AtomicReference<>();
  }

  public final boolean isSet() {
    return !StringHelper.isNullOrEmpty(this.reference.get());
  }

  public final void set(String accessToken) {
    this.reference.set(StringHelper.nullIfEmpty(accessToken));
  }

  @Nullable
  public final String get() {
    return this.reference.get();
  }

  public final void clear() {
    this.reference.set(null);
  }
}
