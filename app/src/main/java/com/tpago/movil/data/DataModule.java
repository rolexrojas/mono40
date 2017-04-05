package com.tpago.movil.data;

import com.google.gson.Gson;
import com.tpago.movil.app.DisplayDensity;
import com.tpago.movil.content.SharedPreferencesCreator;
import com.tpago.movil.domain.BankRepo;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author hecvasro
 */
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
