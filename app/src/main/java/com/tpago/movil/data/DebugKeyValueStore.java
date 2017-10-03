package com.tpago.movil.data;

import android.support.annotation.Nullable;

import com.tpago.movil.domain.KeyValueStore;
import com.tpago.movil.util.ObjectHelper;

import timber.log.Timber;

final class DebugKeyValueStore implements KeyValueStore {

  static DebugKeyValueStore create(KeyValueStore keyValueStore) {
    return new DebugKeyValueStore(keyValueStore);
  }

  private final KeyValueStore keyValueStore;

  private DebugKeyValueStore(KeyValueStore keyValueStore) {
    this.keyValueStore = ObjectHelper.checkNotNull(keyValueStore, "keyValueStore");
  }

  @Override
  public boolean isSet(String key) {
    final boolean isSet = this.keyValueStore.isSet(key);
    Timber.d("isSet(%1$s) = %2$s", key, isSet);
    return isSet;
  }

  @Override
  public void set(String key, String value) {
    this.keyValueStore.set(key, value);
    Timber.d("set(%1$s, %2$s)", key, value);
  }

  @Nullable
  @Override
  public String get(String key) {
    final String value = this.keyValueStore.get(key);
    Timber.d("get(%1$s) = %2$s", key, value);
    return value;
  }

  @Override
  public void remove(String key) {
    this.keyValueStore.remove(key);
    Timber.d("remove(%1$s)", key);
  }
}
