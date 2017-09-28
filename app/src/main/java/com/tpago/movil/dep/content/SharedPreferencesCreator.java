package com.tpago.movil.dep.content;

import android.content.Context;
import android.content.SharedPreferences;

import com.tpago.movil.dep.text.Texts;
import com.tpago.movil.dep.Preconditions;

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
