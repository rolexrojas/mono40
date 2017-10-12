package com.tpago.movil.store;

import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.tpago.movil.util.ObjectHelper;
import com.tpago.movil.util.StringHelper;

/**
 * @author hecvasro
 */
final class GsonSharedPreferencesStore implements Store {

  static GsonSharedPreferencesStore create(Gson gson, SharedPreferences sharedPreferences) {
    return new GsonSharedPreferencesStore(gson, sharedPreferences);
  }

  private final Gson gson;
  private final SharedPreferences sharedPreferences;

  private GsonSharedPreferencesStore(Gson gson, SharedPreferences sharedPreferences) {
    this.gson = ObjectHelper.checkNotNull(gson, "gson");
    this.sharedPreferences = ObjectHelper.checkNotNull(sharedPreferences, "sharedPreferences");
  }

  @Override
  public boolean isSet(String key) {
    return this.sharedPreferences.contains(StringHelper.checkIsNotNullNorEmpty(key, "key"));
  }

  @Override
  public <T> void set(String key, T value) {
    StringHelper.checkIsNotNullNorEmpty(key, "key");
    ObjectHelper.checkNotNull(value, "value");

    this.sharedPreferences.edit()
      .putString(key, this.gson.toJson(value, value.getClass()))
      .apply();
  }

  @Nullable
  @Override
  public <T> T get(String key, Class<T> valueType) {
    StringHelper.checkIsNotNullNorEmpty(key, "key");
    ObjectHelper.checkNotNull(valueType, "valueType");

    T value = null;
    final String json = this.sharedPreferences.getString(key, null);
    if (!StringHelper.isNullOrEmpty(json)) {
      value = this.gson.fromJson(json, valueType);
    }
    return value;
  }

  @Override
  public void remove(String key) {
    this.sharedPreferences.edit()
      .remove(StringHelper.checkIsNotNullNorEmpty(key, "key"))
      .apply();
  }
}
