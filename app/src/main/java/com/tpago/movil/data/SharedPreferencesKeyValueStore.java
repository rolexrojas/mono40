package com.tpago.movil.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

import com.tpago.movil.domain.KeyValueStore;
import com.tpago.movil.util.StringHelper;

final class SharedPreferencesKeyValueStore implements KeyValueStore {

  static SharedPreferencesKeyValueStore create(Context context) {
    return new SharedPreferencesKeyValueStore(context);
  }

  private final SharedPreferences sharedPreferences;

  private SharedPreferencesKeyValueStore(Context context) {
    this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
  }

  private static String checkNotNullNorEmpty(String value, String argumentName) {
    if (StringHelper.isNullOrEmpty(value)) {
      throw new IllegalArgumentException(
        String.format("StringHelper.isNullOrEmpty(%1$s)", argumentName)
      );
    }
    return value;
  }

  @Override
  public boolean isSet(String key) {
    return this.sharedPreferences.contains(checkNotNullNorEmpty(key, "key"));
  }

  @Override
  public void set(String key, String value) {
    this.sharedPreferences.edit()
      .putString(
        checkNotNullNorEmpty(key, "key"),
        checkNotNullNorEmpty(value, "value")
      )
      .apply();
  }

  @Nullable
  @Override
  public String get(String key) {
    return this.sharedPreferences.getString(checkNotNullNorEmpty(key, "key"), null);
  }

  @Override
  public void remove(String key) {
    this.sharedPreferences.edit()
      .remove(checkNotNullNorEmpty(key, "key"))
      .apply();
  }
}
