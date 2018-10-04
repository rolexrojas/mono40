package com.tpago.movil.company.bank;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author hecvasro
 */
@Module
public final class BankModule {

  @Provides
  @Singleton
  BankStore bankStore() {
    return BankStoreMemoized.create();
  }
}
