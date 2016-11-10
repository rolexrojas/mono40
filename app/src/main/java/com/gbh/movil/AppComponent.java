package com.gbh.movil;

import com.gbh.movil.data.DataModule;
import com.gbh.movil.data.MessageHelper;
import com.gbh.movil.data.SchedulerProvider;
import com.gbh.movil.domain.AccountManager;
import com.gbh.movil.domain.BalanceManager;
import com.gbh.movil.data.net.NetworkHelper;
import com.gbh.movil.domain.NotificationHolder;
import com.gbh.movil.domain.RecipientRepo;
import com.gbh.movil.domain.api.ApiBridge;

import javax.inject.Singleton;

import dagger.Component;

/**
 * @author hecvasro
 */
@Singleton
@Component(modules = { AppModule.class, DataModule.class })
public interface AppComponent {
  MessageHelper provideMessageHelper();

  NetworkHelper provideNetworkHelper();

  SchedulerProvider provideSchedulerProvider();

  ApiBridge provideApiBridge();

  RecipientRepo provideRecipientRepo();

  NotificationHolder provideNotificationHolder();

  AccountManager provideAccountManager();

  BalanceManager provideBalanceManager();
}
