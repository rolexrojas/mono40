package com.tpago.movil.content;

import android.content.Context;
import android.content.SharedPreferences;

import com.tpago.movil.text.Texts;
import com.tpago.movil.util.Preconditions;

/**
 * @author hecvasro
 */
@Deprecated
public final class SharedPreferencesCreator {
  private final Context context;

  public SharedPreferencesCreator(Context context) {
    this.context = Preconditions.assertNotNull(context, "context == null");
  }

  public final SharedPreferences create(String fileName) {
    if (Texts.checkIfEmpty(fileName)) {
      throw new IllegalArgumentException("Texts.checkIfEmpty(fileName) == true");
    }
    return context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
  }
}
