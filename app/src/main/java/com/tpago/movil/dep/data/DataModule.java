package com.tpago.movil.dep.data;

import com.tpago.movil.DisplayDensity;

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
}
