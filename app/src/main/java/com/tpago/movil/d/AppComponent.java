package com.tpago.movil.d;

import android.content.Context;

import com.tpago.movil.d.data.DataModule;
import com.tpago.movil.d.data.NfcHandler;
import com.tpago.movil.d.data.StringHelper;
import com.tpago.movil.d.data.SchedulerProvider;
import com.tpago.movil.d.data.res.AssetProvider;
import com.tpago.movil.d.domain.InitialDataLoader;
import com.tpago.movil.d.domain.ProductManager;
import com.tpago.movil.d.domain.BalanceManager;
import com.tpago.movil.d.domain.TransactionManager;
import com.tpago.movil.d.domain.pos.PosBridge;
import com.tpago.movil.d.domain.session.SessionManager;
import com.tpago.movil.d.domain.util.EventBus;
import com.tpago.movil.d.domain.RecipientManager;
import com.tpago.movil.d.domain.TransactionRepo;
import com.tpago.movil.d.domain.api.ApiBridge;

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
