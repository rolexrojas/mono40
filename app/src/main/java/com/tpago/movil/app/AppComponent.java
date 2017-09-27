package com.tpago.movil.app;

import android.content.Context;

import com.tpago.movil.ConfigManager;
import com.tpago.movil.UserStore;
import com.tpago.movil.content.ContentModule;
import com.tpago.movil.d.DepAppModule;
import com.tpago.movil.api.ApiModule;
import com.tpago.movil.d.data.DepDataModule;
import com.tpago.movil.d.data.SchedulerProvider;
import com.tpago.movil.d.data.StringHelper;
import com.tpago.movil.d.domain.BalanceManager;
import com.tpago.movil.d.domain.InitialDataLoader;
import com.tpago.movil.d.domain.ProductManager;
import com.tpago.movil.d.domain.RecipientManager;
import com.tpago.movil.d.domain.TransactionRepo;
import com.tpago.movil.d.domain.api.DepApiBridge;
import com.tpago.movil.d.domain.pos.PosBridge;
import com.tpago.movil.d.domain.session.SessionManager;
import com.tpago.movil.d.domain.util.EventBus;
import com.tpago.movil.d.ui.main.recipient.addition.NonAffiliatedPhoneNumberRecipientAddition1Fragment;
import com.tpago.movil.d.ui.main.recipient.addition.NonAffiliatedPhoneNumberRecipientAddition2Fragment;
import com.tpago.movil.d.ui.main.recipient.index.disburse.DisbursementActivity;
import com.tpago.movil.d.ui.main.transaction.own.OwnTransactionCreationActivity;
import com.tpago.movil.d.ui.main.transaction.own.OwnTransferActivity;
import com.tpago.movil.data.DataModule;
import com.tpago.movil.data.StringMapper;
import com.tpago.movil.domain.BankProvider;
import com.tpago.movil.domain.DomainModule;
import com.tpago.movil.gson.GsonModule;
import com.tpago.movil.init.InitComponent;
import com.tpago.movil.init.InitModule;
import com.tpago.movil.main.MainComponent;
import com.tpago.movil.main.MainModule;
import com.tpago.movil.net.NetModule;
import com.tpago.movil.net.NetworkService;

import javax.inject.Singleton;

import dagger.Component;

/**
 * @author hecvasro
 */
@Singleton
@Component(modules = {
  AppModule.class,
  ContentModule.class,
  DataModule.class,
  DomainModule.class,
  NetModule.class,
  GsonModule.class,
  ApiModule.class,
  // Deprecated modules
  DepAppModule.class,
  DepDataModule.class
})
public interface AppComponent {

  StringMapper stringMapper();

  InitComponent plus(ActivityModule activityModule, InitModule initModule);

  MainComponent plus(ActivityModule activityModule, MainModule mainModule);

  void inject(App app);

  void inject(OwnTransactionCreationActivity activity);

  void inject(OwnTransferActivity activity);

  void inject(DisbursementActivity activity);

  // Deprecated injects
  void inject(NonAffiliatedPhoneNumberRecipientAddition1Fragment fragment);

  void inject(NonAffiliatedPhoneNumberRecipientAddition2Fragment fragment);

  // Deprecated provides
  BalanceManager provideBalanceManager();

  BankProvider provideBankProvider();

  ConfigManager provideConfigManager();

  Context provideContext();

  DepApiBridge provideApiBridge();

  EventBus provideEventBus();

  InitialDataLoader provideInitialDataLoader();

  NetworkService provideNetworkService();

  PosBridge providePosBridge();

  ProductManager provideProductManager();

  RecipientManager provideRecipientManager();

  SchedulerProvider provideSchedulerProvider();

  SessionManager provideSessionManager();

  StringHelper provideStringHelper();

  TransactionRepo provideTransactionRepo();

  UserStore provideUserStore();
}
