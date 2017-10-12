package com.tpago.movil.dep.data;

import com.google.gson.Gson;
import com.tpago.movil.DisplayDensity;
import com.tpago.movil.dep.content.SharedPreferencesCreator;
import com.tpago.movil.d.domain.BankRepo;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Deprecated
@Module
public final class DataModule {

  @Provides
  @Singleton
  AssetUriBuilder provideAssetUriBuilder(DisplayDensity displayDensity) {
    return new AssetUriBuilder(displayDensity);
  }

  @Provides
  @Singleton
  BankRepo provideBankRepo(Gson gson, SharedPreferencesCreator sharedPreferencesCreator) {
    return new SharedPreferencesBankRepo(gson, sharedPreferencesCreator);
  }
}
