package com.tpago.movil.data;

import com.google.gson.Gson;
import com.tpago.movil.app.DisplayDensity;
import com.tpago.movil.content.SharedPreferencesCreator;
import com.tpago.movil.domain.BankRepo;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * {@link Module} that contains providers for objects that belong to the data layer.
 *
 * @author hecvasro
 */
@Module(includes = DataFlavorModule.class)
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
