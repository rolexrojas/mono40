package com.mono40.movil.store;

import android.content.SharedPreferences;

import com.google.gson.Gson;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author hecvasro
 */
@Module
public final class DiskStoreModule {

  @Provides
  @Singleton
  DiskStore store(Gson gson, SharedPreferences sharedPreferences) {
    return DiskStoreGsonPreferences.create(gson, sharedPreferences);
  }
}
