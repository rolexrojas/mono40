package com.gbh.movil.domain;

import android.support.annotation.Nullable;

/**
 * TODO
 *
 * @author hecvasro
 */
public final class SessionManager {
  private static final String TOKEN = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxNDo4MjkzNDUxMjI3OjAxMjM0NTY3ODkwMTIzNCIsImV4cCI6MTQ4MTc0MzA4N30.TNo8_Kvweck-NBiuHIdEVvwUvTodGgiyWKgGUHEwVUI";

  /**
   * TODO
   *
   * @return TODO
   */
  public final boolean isActive() {
    return true;
  }

  /**
   * TODO
   *
   * @return TODO
   */
  @Nullable
  public final String getAuthToken() {
    return TOKEN;
  }
}
