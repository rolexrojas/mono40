package com.gbh.movil;

import android.content.Context;

import com.gbh.movil.data.DataModule;
import com.gbh.movil.data.StringHelper;
import com.gbh.movil.data.SchedulerProvider;
import com.gbh.movil.data.res.AssetProvider;
import com.gbh.movil.domain.ProductManager;
import com.gbh.movil.domain.BalanceManager;
import com.gbh.movil.data.net.NetworkHelper;
import com.gbh.movil.domain.TransactionManager;
import com.gbh.movil.domain.pos.PosBridge;
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
  Context provideContext();

  StringHelper provideStringHelper();

  NetworkHelper provideNetworkHelper();

  SchedulerProvider provideSchedulerProvider();

  AssetProvider provideResourceProvider();

  EventBus provideEventBus();

  ApiBridge provideApiBridge();

  PosBridge providePosBridge();

  TransactionRepo provideTransactionRepo();

  ProductManager provideProductManager();

  BalanceManager provideBalanceManager();

  RecipientManager provideRecipientManager();

  TransactionManager provideTransactionManager();
}
