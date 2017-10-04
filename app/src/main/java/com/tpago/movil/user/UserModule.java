package com.tpago.movil.user;

import com.google.gson.Gson;
import com.tpago.movil.KeyValueStore;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author hecvasro
 */
@Module
public final class UserModule {

  @Provides
  @Singleton
  UserStore userStore(Gson gson, KeyValueStore keyValueStore) {
    return GsonKeyValueUserStore.create(gson, keyValueStore);
  }
}
