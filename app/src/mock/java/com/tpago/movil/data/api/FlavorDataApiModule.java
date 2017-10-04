package com.tpago.movil.data.api;

import com.tpago.movil.data.auth.alt.AltAuthMethodConfigData;
import com.tpago.movil.KeyValueStore;
import com.tpago.movil.api.Api;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author hecvasro
 */
@Module
public final class FlavorDataApiModule {

  @Provides
  @Singleton
  Api api(KeyValueStore keyValueStore, AltAuthMethodConfigData altAuthMethodConfigData) {
    return MockApi.builder()
      .keyValueStore(keyValueStore)
      .altAuthMethodConfigData(altAuthMethodConfigData)
      .build();
  }
}
