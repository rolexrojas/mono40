package com.tpago.movil.d.domain;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tpago.movil.d.misc.Utils;
import com.google.i18n.phonenumbers.NumberParseException;

/**
 * TODO
 *
 * @author hecvasro
 */
@Deprecated
public final class SessionManager {
  private static SessionManager INSTANCE;

  private PhoneNumber phoneNumber;
  private String token;

  private SessionManager() {
    try {
      phoneNumber = new PhoneNumber("8093451227");
    } catch (NumberParseException exception) {
    }
    token = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxNDo4MjkzNDUxMjI3OjAxMjM0NTY3ODkwMTIzNCIsImV4cCI6MTQ4MTc0MzA4N30.TNo8_Kvweck-NBiuHIdEVvwUvTodGgiyWKgGUHEwVUI";
  }

  @NonNull
  public static SessionManager getInstance() {
    if (Utils.isNull(INSTANCE)) {
      INSTANCE = new SessionManager();
    }
    return INSTANCE;
  }

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
  public final PhoneNumber getPhoneNumber() {
    return phoneNumber;
  }

  /**
   * TODO
   *
   * @return TODO
   */
  @Nullable
  public final String getAuthToken() {
    return token;
  }
}
