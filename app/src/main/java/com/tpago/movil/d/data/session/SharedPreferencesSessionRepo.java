package com.tpago.movil.d.data.session;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tpago.movil.d.domain.session.Session;
import com.tpago.movil.d.domain.session.SessionRepo;
import com.tpago.movil.d.domain.text.TextHelper;
import com.tpago.movil.d.misc.Utils;

/**
 * TODO
 *
 * @author hecvasro
 */
class SharedPreferencesSessionRepo implements SessionRepo {
  /**
   * TODO
   */
  private static final String FILE_NAME = String.format("%1$s.%2$s",
    SessionRepo.class.getPackage().getName(), SessionRepo.class.getSimpleName());

  /**
   * TODO
   */
  private static final String KEY_PHONE_NUMBER = "phoneNumber";
  /**
   * TODO
   */
  private static final String KEY_EMAIL = "email";
  /**
   * TODO
   */
  private static final String KEY_AUTH_TOKEN = "authToken";

  private final SharedPreferences sharedPreferences;

  /**
   * TODO
   */
  private Session session;

  /**
   * TODO
   *
   * @param context
   *   TODO
   */
  SharedPreferencesSessionRepo(@NonNull Context context) {
    sharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
  }

  @Nullable
  @Override
  public Session getSession() {
    if (Utils.isNull(session) && hasSession()) {
      final String phoneNumber = sharedPreferences.getString(KEY_PHONE_NUMBER, null);
      final String email = sharedPreferences.getString(KEY_EMAIL, null);
      final String authToken = sharedPreferences.getString(KEY_AUTH_TOKEN, null);
      if (TextHelper.isEmpty(phoneNumber) && TextHelper.isEmpty(email)
        && TextHelper.isEmpty(authToken)) {
        clearSession();
      } else {
        session = new Session(phoneNumber, email, authToken);
      }
    }
    return session;
  }

  @Override
  public boolean hasSession() {
    return sharedPreferences.contains(KEY_EMAIL) && sharedPreferences.contains(KEY_AUTH_TOKEN);
  }

  @Override
  public void clearSession() {
    sharedPreferences.edit()
      .clear()
      .apply();
  }

  @Override
  public void setSession(@NonNull Session session) {
    this.session = session;
    this.sharedPreferences.edit()
      .putString(KEY_PHONE_NUMBER, session.getPhoneNumber())
      .putString(KEY_EMAIL, session.getEmail())
      .putString(KEY_AUTH_TOKEN, session.getAuthToken())
      .apply();
  }
}
