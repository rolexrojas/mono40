package com.tpago.movil.data.api;

import android.content.Context;

import com.tpago.movil.session.AccessTokenStore;
import com.tpago.movil.session.UnlockMethodConfigData;
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
    UnlockMethodConfigData configData,
    Context context,
    Store store
  ) {
    return MockApi.builder()
      .accessTokenStore(accessTokenStore)
      .configData(configData)
      .context(context)
      .store(store)
      .build();
  }
}
