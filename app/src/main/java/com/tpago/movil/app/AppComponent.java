package com.tpago.movil.app;

import android.content.Context;

import com.tpago.movil.dep.DepAppModule;
import com.tpago.movil.api.ApiModule;
import com.tpago.movil.dep.data.DepDataModule;
import com.tpago.movil.dep.data.NfcHandler;
import com.tpago.movil.dep.data.SchedulerProvider;
import com.tpago.movil.dep.data.StringHelper;
import com.tpago.movil.dep.data.res.AssetProvider;
import com.tpago.movil.dep.domain.BalanceManager;
import com.tpago.movil.dep.domain.InitialDataLoader;
import com.tpago.movil.dep.domain.ProductManager;
import com.tpago.movil.dep.domain.RecipientManager;
import com.tpago.movil.dep.domain.TransactionManager;
import com.tpago.movil.dep.domain.TransactionRepo;
import com.tpago.movil.dep.domain.api.ApiBridge;
import com.tpago.movil.dep.domain.pos.PosBridge;
import com.tpago.movil.dep.domain.session.SessionManager;
import com.tpago.movil.dep.domain.util.EventBus;
import com.tpago.movil.init.InitComponent;
import com.tpago.movil.init.InitModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * @author hecvasro
 */
@Singleton
@Component(modules = {
  AppModule.class,
  ApiModule.class,
  // Deprecated modules
  DepAppModule.class,
  DepDataModule.class
})
public interface AppComponent {
  InitComponent plus(InitModule module);

  void inject(AvatarCreationDialogFragment fragment);

  // Deprecated provides
  ApiBridge provideApiBridge();
  AssetProvider provideResourceProvider();
  BalanceManager provideBalanceManager();
  Context provideContext();
  EventBus provideEventBus();
  InitialDataLoader provideInitialDataLoader();
  NfcHandler provideNfcHandler();
  PosBridge providePosBridge();
  ProductManager provideProductManager();
  RecipientManager provideRecipientManager();
  SchedulerProvider provideSchedulerProvider();
  SessionManager provideSessionManager();
  StringHelper provideStringHelper();
  TransactionManager provideTransactionManager();
  TransactionRepo provideTransactionRepo();
}
