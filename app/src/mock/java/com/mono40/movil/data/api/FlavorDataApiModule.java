package com.mono40.movil.data.api;

import android.content.Context;

import com.mono40.movil.session.AccessTokenStore;
import com.mono40.movil.session.UnlockMethodConfigData;
import com.mono40.movil.store.DiskStore;
import com.mono40.movil.api.Api;

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
    DiskStore diskStore
  ) {
    return MockApi.builder()
      .accessTokenStore(accessTokenStore)
      .configData(configData)
      .context(context)
      .store(diskStore)
      .build();
  }
}
