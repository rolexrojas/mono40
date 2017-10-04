package com.tpago.movil;

import android.support.annotation.Nullable;

/**
 * @author hecvasro
 */
public interface KeyValueStore {

  boolean isSet(String key);

  void set(String key, String value);

  @Nullable
  String get(String key);

  void remove(String key);
}
