package com.gbh.movil.data.session;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gbh.movil.domain.session.Session;
import com.gbh.movil.domain.session.SessionRepo;
import com.gbh.movil.domain.text.TextHelper;
import com.gbh.movil.misc.Utils;

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
  private static final String KEY_NAME = "name";
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
      final String name = sharedPreferences.getString(KEY_NAME, null);
      final String authToken = sharedPreferences.getString(KEY_AUTH_TOKEN, null);
      if (TextHelper.isNotEmpty(name) && TextHelper.isNotEmpty(authToken)) {
        session = new Session(name, authToken);
      } else {
        clearSession();
      }
    }
    return session;
  }

  @Override
  public boolean hasSession() {
    return sharedPreferences.contains(KEY_NAME) && sharedPreferences.contains(KEY_AUTH_TOKEN);
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
      .putString(KEY_NAME, session.getName())
      .putString(KEY_AUTH_TOKEN, session.getAuthToken())
      .apply();
  }
}
