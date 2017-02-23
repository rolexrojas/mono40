package com.tpago.movil.api;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author hecvasro
 */
@Module
public class ApiModule {
  @Provides
  @Singleton
  ApiBridge provideApiBridge() {
    return new FakeApiBridge();
  }
}
