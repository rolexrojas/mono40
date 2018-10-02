package com.tpago.movil.store;

import io.reactivex.Completable;
import io.reactivex.Maybe;

/**
 * @author hecvasro
 */
public interface DiskStore {

  boolean isSet(String key);

  Completable remove(String key);

  <T> Completable set(String key, T value);

  <T> Maybe<T> get(String key, Class<T> valueType);
}
