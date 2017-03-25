package com.tpago.movil.d;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.tpago.movil.app.App;
import com.tpago.movil.content.SharedPreferencesCreator;
import com.tpago.movil.d.domain.InitialDataLoader;
import com.tpago.movil.d.domain.ProductManager;
import com.tpago.movil.d.domain.BalanceManager;
import com.tpago.movil.d.domain.TransactionManager;
import com.tpago.movil.d.domain.api.DepApiBridge;
import com.tpago.movil.d.domain.pos.PosBridge;
import com.tpago.movil.d.domain.session.SessionManager;
import com.tpago.movil.d.domain.util.EventBus;
import com.tpago.movil.d.domain.RecipientManager;

import javax.inject.Singleton;

import dagger.Lazy;
import dagger.Module;
import dagger.Provides;

/**
 * @author hecvasro
 */
@Module
@Deprecated
public final class DepAppModule {
  private final App app;

  public DepAppModule(@NonNull App app) {
    this.app = app;
  }

  @Provides
  @Singleton
  InitialDataLoader provideInitialDataLoader(
    DepApiBridge apiBridge,
    ProductManager productManager,
    RecipientManager recipientManager) {
    return new InitialDataLoader(apiBridge, productManager, recipientManager);
  }

  @Provides
  @Singleton
  EventBus provideEventBus() {
    return new EventBus();
  }

  @Provides
  @Singleton
  BalanceManager provideBalanceManager(
    EventBus eventBus,
    DepApiBridge apiBridge,
    SessionManager sessionManager) {
    return new BalanceManager(eventBus, apiBridge, sessionManager);
  }

  @Provides
  @Singleton
  ProductManager provideProductManager(
    SharedPreferencesCreator sharedPreferencesCreator,
    Gson gson,
    EventBus eventBus,
    DepApiBridge apiBridge,
    Lazy<PosBridge> posBridge) {
    return new ProductManager(sharedPreferencesCreator, gson, eventBus, apiBridge, posBridge);
  }

  @Provides
  @Singleton
  RecipientManager provideRecipientManager(
    SharedPreferencesCreator sharedPreferencesCreator,
    Gson gson,
    DepApiBridge apiBridge) {
    return new RecipientManager(sharedPreferencesCreator, gson, apiBridge);
  }

  @Provides
  @Singleton
  TransactionManager provideTransactionManager(DepApiBridge apiBridge, SessionManager sessionManager) {
    return new TransactionManager(apiBridge, sessionManager);
  }
}
