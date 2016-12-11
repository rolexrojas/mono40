package com.gbh.movil;

import android.content.Context;

import com.gbh.movil.data.DataModule;
import com.gbh.movil.data.StringHelper;
import com.gbh.movil.data.SchedulerProvider;
import com.gbh.movil.data.res.AssetProvider;
import com.gbh.movil.domain.ProductManager;
import com.gbh.movil.domain.BalanceManager;
import com.gbh.movil.domain.TransactionManager;
import com.gbh.movil.domain.pos.PosBridge;
import com.gbh.movil.domain.session.SessionManager;
import com.gbh.movil.domain.util.EventBus;
import com.gbh.movil.domain.RecipientManager;
import com.gbh.movil.domain.TransactionRepo;
import com.gbh.movil.domain.api.ApiBridge;

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
  PosBridge providePosBridge();
  ProductManager provideProductManager();
  RecipientManager provideRecipientManager();
  SchedulerProvider provideSchedulerProvider();
  StringHelper provideStringHelper();
  TransactionManager provideTransactionManager();
  TransactionRepo provideTransactionRepo();
}
