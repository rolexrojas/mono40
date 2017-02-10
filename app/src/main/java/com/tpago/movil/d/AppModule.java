package com.tpago.movil.d;

import android.content.Context;
import android.support.annotation.NonNull;

import com.tpago.movil.d.domain.InitialDataLoader;
import com.tpago.movil.d.domain.ProductManager;
import com.tpago.movil.d.domain.ProductRepo;
import com.tpago.movil.d.domain.BalanceManager;
import com.tpago.movil.d.domain.TransactionManager;
import com.tpago.movil.d.domain.pos.PosBridge;
import com.tpago.movil.d.domain.session.SessionManager;
import com.tpago.movil.d.domain.util.EventBus;
import com.tpago.movil.d.domain.RecipientManager;
import com.tpago.movil.d.domain.RecipientRepo;
import com.tpago.movil.d.domain.api.ApiBridge;

import javax.inject.Singleton;

import dagger.Lazy;
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
  InitialDataLoader provideInitialDataLoader(ApiBridge apiBridge, ProductManager productManager,
    RecipientManager recipientManager, SessionManager sessionManager) {
    return new InitialDataLoader(apiBridge, productManager, recipientManager, sessionManager);
  }

  @Provides
  @Singleton
  EventBus provideEventBus() {
    return new EventBus();
  }

  @Provides
  @Singleton
  ProductManager provideProductManager(ProductRepo productRepo, Lazy<PosBridge> posBridge,
    EventBus eventBus, SessionManager sessionManager) {
    return new ProductManager(productRepo, posBridge, eventBus, sessionManager);
  }

  @Provides
  @Singleton
  BalanceManager provideBalanceManager(EventBus eventBus, ApiBridge apiBridge,
    SessionManager sessionManager) {
    return new BalanceManager(eventBus, apiBridge, sessionManager);
  }

  @Provides
  @Singleton
  RecipientManager provideRecipientManager(RecipientRepo recipientRepo, ApiBridge apiBridge,
    SessionManager sessionManager) {
    return new RecipientManager(recipientRepo, apiBridge, sessionManager);
  }

  @Provides
  @Singleton
  TransactionManager provideTransactionManager(ApiBridge apiBridge, SessionManager sessionManager) {
    return new TransactionManager(apiBridge, sessionManager);
  }
}
