package com.gbh.movil;

import android.content.Context;
import android.support.annotation.NonNull;

import com.gbh.movil.domain.AccountRepository;
import com.gbh.movil.domain.BalanceManager;
import com.gbh.movil.domain.DataLoader;
import com.gbh.movil.domain.api.ApiBridge;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * TODO
 *
 * @author hecvasro
 */
@Module
final class AppModule {
  private final App app;

  AppModule(@NonNull App app) {
    this.app = app;
  }

  /**
   * TODO
   *
   * @return TODO
   */
  @Provides
  @Singleton
  Context provideContext() {
    return app;
  }

  /**
   * TODO
   *
   * @param apiBridge
   *   TODO
   * @param accountRepository
   *   TODO
   *
   * @return TODO
   */
  @Provides
  @Singleton
  DataLoader provideDataLoader(ApiBridge apiBridge, AccountRepository accountRepository) {
    return new DataLoader(apiBridge, accountRepository);
  }

  /**
   * TODO
   *
   * @param apiBridge
   *   TODO
   *
   * @return TODO
   */
  @Provides
  @Singleton
  BalanceManager provideBalanceManager(ApiBridge apiBridge) {
    return new BalanceManager(apiBridge);
  }
}
