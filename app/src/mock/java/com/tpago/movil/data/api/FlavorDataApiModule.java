package com.tpago.movil.data.api;

import com.tpago.movil.data.auth.alt.AltAuthMethodConfigData;
import com.tpago.movil.session.AccessTokenStore;
import com.tpago.movil.store.Store;
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
  Api api(
    AccessTokenStore accessTokenStore,
    AltAuthMethodConfigData altAuthMethodConfigData,
    Store store
  ) {
    return MockApi.builder()
      .accessTokenStore(accessTokenStore)
      .altAuthMethodConfigData(altAuthMethodConfigData)
      .store(store)
      .build();
  }
}
