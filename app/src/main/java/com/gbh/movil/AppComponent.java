package com.gbh.movil;

import android.content.Context;

import com.gbh.movil.data.DataModule;
import com.gbh.movil.data.StringHelper;
import com.gbh.movil.data.SchedulerProvider;
import com.gbh.movil.domain.ProductManager;
import com.gbh.movil.domain.BalanceManager;
import com.gbh.movil.data.net.NetworkHelper;
import com.gbh.movil.domain.util.EventBus;
import com.gbh.movil.domain.RecipientManager;
import com.gbh.movil.domain.SessionManager;
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

  EventBus provideNotificationHolder();

  SessionManager provideSessionManager();

  ApiBridge provideApiBridge();

  TransactionRepo provideTransactionRepo();

  ProductManager provideProductManager();

  BalanceManager provideBalanceManager();

  RecipientManager provideRecipientManager();
}
