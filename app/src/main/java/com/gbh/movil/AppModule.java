package com.gbh.movil;

import android.content.Context;
import android.support.annotation.NonNull;

import com.gbh.movil.domain.AccountManager;
import com.gbh.movil.domain.AccountRepo;
import com.gbh.movil.domain.BalanceManager;
import com.gbh.movil.data.net.NetworkHelper;
import com.gbh.movil.domain.api.ApiBridge;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author hecvasro
 */
@Module
final class AppModule {
  private final App app;

  AppModule(@NonNull App app) {
    this.app = app;
  }

  @Provides
  @Singleton
  Context provideContext() {
    return app;
  }

  @Provides
  @Singleton
  AccountManager provideAccountManager(AccountRepo accountRepo) {
    return new AccountManager(accountRepo);
  }

  @Provides
  @Singleton
  BalanceManager provideBalanceManager(NetworkHelper networkHelper, ApiBridge apiBridge) {
    return new BalanceManager(networkHelper, apiBridge);
  }
}
