package com.tpago.movil.data.api;

import com.tpago.movil.domain.api.ApiBridge;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * TODO
 *
 * @author hecvasro
 */
@Module
public class ApiModule {
  /**
   * TODO
   *
   * @return TODO
   */
  @Provides
  @Singleton
  ApiBridge provideApiBridge() {
    return new FakeApiBridge();
  }
}
