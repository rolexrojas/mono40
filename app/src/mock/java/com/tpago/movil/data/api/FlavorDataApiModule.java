package com.tpago.movil.data.api;

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
  Api api() {
    return MockApi.create();
  }
}
