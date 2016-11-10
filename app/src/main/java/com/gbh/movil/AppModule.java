package com.gbh.movil;

import android.content.Context;
import android.support.annotation.NonNull;

import com.gbh.movil.domain.AccountManager;
import com.gbh.movil.domain.AccountRepo;
import com.gbh.movil.domain.BalanceManager;
import com.gbh.movil.domain.EventBus;
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
  EventBus provideNotificationHolder() {
    return new EventBus();
  }

  @Provides
  @Singleton
  AccountManager provideAccountManager(EventBus eventBus, AccountRepo accountRepo,
    ApiBridge apiBridge) {
    return new AccountManager(eventBus, accountRepo, apiBridge);
  }

  @Provides
  @Singleton
  BalanceManager provideBalanceManager(EventBus eventBus, ApiBridge apiBridge) {
    return new BalanceManager(eventBus, apiBridge);
  }
}
