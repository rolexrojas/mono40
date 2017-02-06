package com.tpago.movil;

import android.content.Context;

import com.tpago.movil.data.DataModule;
import com.tpago.movil.data.NfcHandler;
import com.tpago.movil.data.StringHelper;
import com.tpago.movil.data.SchedulerProvider;
import com.tpago.movil.data.res.AssetProvider;
import com.tpago.movil.domain.InitialDataLoader;
import com.tpago.movil.domain.ProductManager;
import com.tpago.movil.domain.BalanceManager;
import com.tpago.movil.domain.TransactionManager;
import com.tpago.movil.domain.pos.PosBridge;
import com.tpago.movil.domain.session.SessionManager;
import com.tpago.movil.domain.util.EventBus;
import com.tpago.movil.domain.RecipientManager;
import com.tpago.movil.domain.TransactionRepo;
import com.tpago.movil.domain.api.ApiBridge;

import javax.inject.Singleton;

import dagger.Component;

/**
 * @author hecvasro
 */
@Singleton
@Component(modules = { AppModule.class, DataModule.class })
public interface AppComponent {
  SessionManager provideSessionManager();

  ApiBridge provideApiBridge();
  AssetProvider provideResourceProvider();
  BalanceManager provideBalanceManager();
  Context provideContext();
  EventBus provideEventBus();
  InitialDataLoader provideInitialDataLoader();
  PosBridge providePosBridge();
  ProductManager provideProductManager();
  RecipientManager provideRecipientManager();
  SchedulerProvider provideSchedulerProvider();
  StringHelper provideStringHelper();
  TransactionManager provideTransactionManager();
  TransactionRepo provideTransactionRepo();
  NfcHandler provideNfcHandler();
}
