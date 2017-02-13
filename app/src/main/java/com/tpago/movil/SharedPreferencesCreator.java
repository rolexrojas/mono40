package com.tpago.movil;

import android.content.Context;
import android.content.SharedPreferences;

import com.tpago.movil.util.Objects;
import com.tpago.movil.util.Strings;

/**
 * @author hecvasro
 */
public final class SharedPreferencesCreator {
  private final Context context;

  public SharedPreferencesCreator(Context context) {
    if (Objects.isNull(context)) {
      throw new NullPointerException("Null context");
    }
    this.context = context;
  }

  public final SharedPreferences create(String fileName) {
    if (Objects.isNull(fileName)) {
      throw new NullPointerException("Null fileName");
    }
    if (Strings.isEmpty(fileName)) {
      throw new IllegalArgumentException("Invalid fileName");
    }
    return context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
  }
}
