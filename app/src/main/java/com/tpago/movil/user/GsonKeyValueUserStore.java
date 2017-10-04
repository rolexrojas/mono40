package com.tpago.movil.user;

import com.google.gson.Gson;
import com.tpago.movil.KeyValueStore;
import com.tpago.movil.KeyValueStoreHelper;
import com.tpago.movil.util.ObjectHelper;

/**
 * @author hecvasro
 */
final class GsonKeyValueUserStore implements UserStore {

  private static final String KEY = KeyValueStoreHelper
    .createKey(GsonKeyValueUserStore.class, "Data");

  static GsonKeyValueUserStore create(Gson gson, KeyValueStore keyValueStore) {
    return new GsonKeyValueUserStore(gson, keyValueStore);
  }

  private final Gson gson;
  private final KeyValueStore keyValueStore;

  private GsonKeyValueUserStore(Gson gson, KeyValueStore keyValueStore) {
    this.gson = ObjectHelper.checkNotNull(gson, "gson");
    this.keyValueStore = ObjectHelper.checkNotNull(keyValueStore, "keyValueStore");
  }

  @Override
  public void set(User user) {
    ObjectHelper.checkNotNull(user, "user");

    this.keyValueStore.set(KEY, this.gson.toJson(user, User.class));
  }

  @Override
  public boolean isSet() {
    return this.keyValueStore.isSet(KEY);
  }

  @Override
  public User get() {
    return this.isSet() ? this.gson.fromJson(this.keyValueStore.get(KEY), User.class) : null;
  }

  @Override
  public void clear() {
    this.keyValueStore.remove(KEY);
  }
}
