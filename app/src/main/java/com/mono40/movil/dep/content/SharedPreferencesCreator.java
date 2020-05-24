package com.mono40.movil.dep.content;

import android.content.Context;
import android.content.SharedPreferences;

import com.mono40.movil.util.ObjectHelper;
import com.mono40.movil.util.StringHelper;

/**
 * @author hecvasro
 */
@Deprecated
public final class SharedPreferencesCreator {

  private final Context context;

  public SharedPreferencesCreator(Context context) {
    this.context = ObjectHelper.checkNotNull(context, "context");
  }

  public final SharedPreferences create(String fileName) {
    if (StringHelper.isNullOrEmpty(fileName)) {
      throw new IllegalArgumentException("StringHelper.isNullOrEmpty(fileName)");
    }
    return context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
  }
}
