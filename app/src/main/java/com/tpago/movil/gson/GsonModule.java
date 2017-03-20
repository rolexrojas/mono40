package com.tpago.movil.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapterFactory;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author hecvasro
 */
@Module
public final class GsonModule {
  @Provides
  @Singleton
  TypeAdapterFactory provideTypeAdapterFactory() {
    return GeneratedTypeAdapterFactory.create();
  }

  @Provides
  @Singleton
  Gson provideGson(TypeAdapterFactory typeAdapterFactory) {
    return new GsonBuilder()
      .registerTypeAdapterFactory(typeAdapterFactory)
      .create();
  }
}
