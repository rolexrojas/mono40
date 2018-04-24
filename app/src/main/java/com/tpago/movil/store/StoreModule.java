package com.tpago.movil.store;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.tpago.movil.BuildConfig;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Module that provides dependencies associated with {@link Store}
 */
@Module
public final class StoreModule {

  @Deprecated
  @Provides
  @Singleton
  Store store(Gson gson, SharedPreferences sharedPreferences) {
    Store store = JsonFileStore.create(gson, sharedPreferences);
    if (BuildConfig.DEBUG) {
      store = DebugStore.create(store);
    }
    return store;
  }

  @Provides
  @Singleton
  DiskStoreFactory diskStoreFactory(Context context, Gson gson) {
    return DiskStoreFactory.builder()
      .context(context)
      .gson(gson)
      .build();
  }
}
