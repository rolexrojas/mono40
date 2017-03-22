package com.tpago.movil.d.data.api;

import com.tpago.movil.d.domain.api.DepApiBridge;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author hecvasro
 */
@Module
public class ApiFlavorModule {
  @Provides
  @Singleton
  DepApiBridge provideDepApiBridge() {
    return new FakeDepApiBridge();
  }
}
