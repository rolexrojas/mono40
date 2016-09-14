package com.gbh.tpago.domain;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * TODO
 *
 * @author hecvasro
 */
public final class SessionManager {
  /**
   * TODO
   */
  private final SharedPreferences sharedPreferences;

  /**
   * TODO
   *
   * @param sharedPreferences TODO
   */
  public SessionManager(@NonNull SharedPreferences sharedPreferences) {
    this.sharedPreferences = sharedPreferences;
  }

  /**
   * TODO
   *
   * @return TODO
   */
  public final boolean isActive() {
    // TODO
    return false;
  }

  /**
   * TODO
   *
   * @return TODO
   */
  @Nullable
  public final String getAuthToken() {
    // TODO
    return null;
  }

  /**
   * TODO
   *
   * @param authToken TODO
   */
  public final void setAuthToken(@Nullable String authToken) {
    // TODO
  }

  /**
   * TODO
   *
   * @return TODO
   */
  @Nullable
  public final String getEncryptionKey() {
    // TODO
    return null;
  }

  /**
   * TODO
   *
   * @param encryptionKey TODO
   */
  public final void setEncryptionKey(@Nullable String encryptionKey) {
    // TODO
  }
}
