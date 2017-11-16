package com.tpago.movil.store;

import android.support.annotation.Nullable;

/**
 * @author hecvasro
 */
public interface Store {

  boolean isSet(String key);

  <T> void set(String key, T value);

  @Nullable
  <T> T get(String key, Class<T> valueType);

  void remove(String key);
}
