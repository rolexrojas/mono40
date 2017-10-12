package com.tpago.movil.store;

import android.support.annotation.Nullable;

import com.tpago.movil.util.ObjectHelper;

import timber.log.Timber;

final class DebugStore implements Store {

  static DebugStore create(Store store) {
    return new DebugStore(store);
  }

  private final Store store;

  private DebugStore(Store store) {
    this.store = ObjectHelper.checkNotNull(store, "store");
  }

  @Override
  public boolean isSet(String key) {
    final boolean isSet = this.store.isSet(key);
    Timber.d("isSet(%1$s) = %2$s", key, isSet);
    return isSet;
  }

  @Override
  public <T> void set(String key, T value) {
    this.store.set(key, value);
    Timber.d("set(%1$s, %2$s)", key, value);
  }

  @Nullable
  @Override
  public <T> T get(String key, Class<T> valueType) {
    final T value = this.store.get(key, valueType);
    Timber.d("get(%1$s) = %2$s", key, value);
    return value;
  }

  @Override
  public void remove(String key) {
    this.store.remove(key);
    Timber.d("remove(%1$s)", key);
  }
}
