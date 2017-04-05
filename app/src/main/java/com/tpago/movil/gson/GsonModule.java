package com.tpago.movil.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.tpago.movil.data.AssetUriBuilder;
import com.tpago.movil.domain.Bank;

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
  Gson provideGson(AssetUriBuilder assetUriBuilder, TypeAdapterFactory typeAdapterFactory) {
    final TypeAdapter<Bank> bankTypeAdapter = new BankTypeAdapter(assetUriBuilder);

    return new GsonBuilder()
      .registerTypeAdapter(Bank.class, bankTypeAdapter)
      .registerTypeAdapterFactory(typeAdapterFactory)
      .create();
  }
}
