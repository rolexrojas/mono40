package com.mono40.movil.d;

import android.content.Context;
import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.mono40.movil.dep.App;
import com.mono40.movil.dep.content.SharedPreferencesCreator;
import com.mono40.movil.d.domain.InitialDataLoader;
import com.mono40.movil.d.domain.ProductManager;
import com.mono40.movil.d.domain.BalanceManager;
import com.mono40.movil.d.domain.api.DepApiBridge;
import com.mono40.movil.d.domain.pos.PosBridge;
import com.mono40.movil.d.domain.util.EventBus;
import com.mono40.movil.d.domain.RecipientManager;

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
    RecipientManager recipientManager,
    Context context) {
    return new InitialDataLoader(apiBridge, productManager, recipientManager, context);
  }

  @Provides
  @Singleton
  EventBus provideEventBus() {
    return new EventBus();
  }

  @Provides
  @Singleton
  BalanceManager provideBalanceManager(EventBus eventBus, DepApiBridge apiBridge) {
    return new BalanceManager(eventBus, apiBridge);
  }

  @Provides
  @Singleton
  ProductManager provideProductManager(
    SharedPreferencesCreator sharedPreferencesCreator,
    Gson gson,
    Context context,
    EventBus eventBus,
    DepApiBridge apiBridge,
    Lazy<PosBridge> posBridge) {
    ProductManager productManager = new ProductManager(
      sharedPreferencesCreator,
      gson,
      context,
      eventBus,
      apiBridge,
      posBridge);
    productManager.setInitialDataLoader(provideInitialDataLoader(apiBridge, productManager, provideRecipientManager(sharedPreferencesCreator,
            gson), context));
    return productManager;
  }

  @Provides
  @Singleton
  RecipientManager provideRecipientManager(SharedPreferencesCreator sharedPreferencesCreator, Gson gson) {
    return new RecipientManager(sharedPreferencesCreator, gson);
  }
}
