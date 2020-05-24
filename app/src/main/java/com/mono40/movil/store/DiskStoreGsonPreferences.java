package com.mono40.movil.store;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.mono40.movil.util.ObjectHelper;
import com.mono40.movil.util.StringHelper;

import io.reactivex.Completable;
import io.reactivex.Maybe;

/**
 * @author hecvasro
 */
final class DiskStoreGsonPreferences implements DiskStore {

  static DiskStoreGsonPreferences create(Gson gson, SharedPreferences preferences) {
    return new DiskStoreGsonPreferences(gson, preferences);
  }

  private final Gson gson;
  private final SharedPreferences preferences;

  private DiskStoreGsonPreferences(Gson gson, SharedPreferences preferences) {
    this.gson = ObjectHelper.checkNotNull(gson, "gson");
    this.preferences = ObjectHelper.checkNotNull(preferences, "preferences");
  }

  @Override
  public boolean isSet(String key) {
    return this.preferences.contains(StringHelper.checkIsNotNullNorEmpty(key, "key"));
  }

  @Override
  public Completable remove(String key) {
    StringHelper.checkIsNotNullNorEmpty(key, "key");
    return Completable.fromAction(() ->
      this.preferences.edit()
        .remove(key)
        .commit()
    );
  }

  @Override
  public <T> Completable set(String key, T value) {
    StringHelper.checkIsNotNullNorEmpty(key, "key");
    ObjectHelper.checkNotNull(value, "value");
    return Completable.fromAction(() ->
      this.preferences.edit()
        .putString(key, this.gson.toJson(value, value.getClass()))
        .commit()
    );
  }

  @Override
  public <T> Maybe<T> get(String key, Class<T> valueType) {
    StringHelper.checkIsNotNullNorEmpty(key, "key");
    ObjectHelper.checkNotNull(valueType, "valueType");
    return Maybe.defer(() -> {
      if (!this.isSet(key)) {
        return Maybe.empty();
      }
      return Maybe.just(this.preferences.getString(key, ""))
        .map((json) -> this.gson.fromJson(json, valueType));
    });
  }
}
