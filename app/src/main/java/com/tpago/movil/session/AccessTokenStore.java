package com.tpago.movil.session;

import androidx.annotation.Nullable;

import com.tpago.movil.util.StringHelper;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @author hecvasro
 */
public final class AccessTokenStore {

  public static AccessTokenStore create() {
    return new AccessTokenStore();
  }

  private final AtomicReference<String> reference = new AtomicReference<>();

  private AccessTokenStore() {
  }

  public final boolean isSet() {
    return !StringHelper.isNullOrEmpty(reference.get());
  }

  public final void set(String accessToken) {
    this.reference.set(StringHelper.checkIsNotNullNorEmpty(accessToken, "accessToken"));
  }

  @Nullable
  public final String get() {
    return this.reference.get();
  }

  public final void clear() {
    this.reference.set(null);
  }
}
