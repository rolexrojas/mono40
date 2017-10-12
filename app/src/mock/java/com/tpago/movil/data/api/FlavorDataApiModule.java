package com.tpago.movil.data.api;

import com.tpago.movil.data.auth.alt.AltAuthMethodConfigData;
import com.tpago.movil.store.Store;
import com.tpago.movil.api.Api;
import com.tpago.movil.session.AccessTokenManager;

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
    AccessTokenManager accessTokenManager,
    Store store,
    AltAuthMethodConfigData altAuthMethodConfigData
  ) {
    return MockApi.builder()
      .accessTokenStore(accessTokenManager)
      .keyValueStore(store)
      .altAuthMethodConfigData(altAuthMethodConfigData)
      .build();
  }
}
