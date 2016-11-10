package com.gbh.movil.data.api;

import com.gbh.movil.domain.api.ApiBridge;

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
