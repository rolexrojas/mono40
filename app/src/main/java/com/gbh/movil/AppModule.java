package com.gbh.movil;

import android.content.Context;
import android.support.annotation.NonNull;

import com.gbh.movil.domain.ProductManager;
import com.gbh.movil.domain.ProductRepo;
import com.gbh.movil.domain.BalanceManager;
import com.gbh.movil.domain.TransactionManager;
import com.gbh.movil.domain.util.EventBus;
import com.gbh.movil.domain.RecipientManager;
import com.gbh.movil.domain.RecipientRepo;
import com.gbh.movil.domain.SessionManager;
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
  EventBus provideEventBus() {
    return new EventBus();
  }

  @Provides
  @Singleton
  SessionManager provideSessionManager() {
    return new SessionManager();
  }

  @Provides
  @Singleton
  ProductManager provideProductManager(ProductRepo productRepo) {
    return new ProductManager(productRepo);
  }

  @Provides
  @Singleton
  BalanceManager provideBalanceManager(EventBus eventBus, ApiBridge apiBridge) {
    return new BalanceManager(eventBus, apiBridge);
  }

  @Provides
  @Singleton
  RecipientManager provideRecipientManager(RecipientRepo recipientRepo, ApiBridge apiBridge) {
    return new RecipientManager(recipientRepo, apiBridge);
  }

  @Provides
  @Singleton
  TransactionManager provideTransactionManager(ApiBridge apiBridge) {
    return new TransactionManager(apiBridge);
  }
}
