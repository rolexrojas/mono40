package com.tpago.movil.data.api;

import com.tpago.movil.data.auth.alt.AltAuthMethodConfigData;
import com.tpago.movil.KeyValueStore;
import com.tpago.movil.api.Api;
import com.tpago.movil.session.AccessTokenStore;

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
  Api api(
    AccessTokenStore accessTokenStore,
    KeyValueStore keyValueStore,
    AltAuthMethodConfigData altAuthMethodConfigData
  ) {
    return MockApi.builder()
      .accessTokenStore(accessTokenStore)
      .keyValueStore(keyValueStore)
      .altAuthMethodConfigData(altAuthMethodConfigData)
      .build();
  }
}
