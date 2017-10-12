package com.tpago.movil.store;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.tpago.movil.BuildConfig;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author hecvasro
 */
@Module
public final class StoreModule {

  @Provides
  @Singleton
  Store store(Gson gson, SharedPreferences sharedPreferences) {
    Store store = GsonSharedPreferencesStore.create(gson, sharedPreferences);
    if (BuildConfig.DEBUG) {
      store = DebugStore.create(store);
    }
    return store;
  }
}
