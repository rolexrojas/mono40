package com.tpago.movil.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
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
  Gson provideGson(AssetUriBuilder assetUriBuilder) {
    final TypeAdapter<Bank> bankTypeAdapter = new BankTypeAdapter(assetUriBuilder);

    return new GsonBuilder()
      .registerTypeAdapter(Bank.class, bankTypeAdapter)
      .registerTypeAdapterFactory(GeneratedTypeAdapterFactory.create())
      .create();
  }
}
