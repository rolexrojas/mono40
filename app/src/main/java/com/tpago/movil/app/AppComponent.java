package com.tpago.movil.app;

import android.content.Context;

import com.tpago.movil.ConfigManager;
import com.tpago.movil.UserStore;
import com.tpago.movil.dep.DepAppModule;
import com.tpago.movil.api.ApiModule;
import com.tpago.movil.dep.data.DepDataModule;
import com.tpago.movil.dep.data.NfcHandler;
import com.tpago.movil.dep.data.SchedulerProvider;
import com.tpago.movil.dep.data.StringHelper;
import com.tpago.movil.dep.data.res.DepAssetProvider;
import com.tpago.movil.dep.domain.BalanceManager;
import com.tpago.movil.dep.domain.InitialDataLoader;
import com.tpago.movil.dep.domain.ProductManager;
import com.tpago.movil.dep.domain.RecipientManager;
import com.tpago.movil.dep.domain.TransactionManager;
import com.tpago.movil.dep.domain.TransactionRepo;
import com.tpago.movil.dep.domain.api.DepApiBridge;
import com.tpago.movil.dep.domain.pos.PosBridge;
import com.tpago.movil.dep.domain.session.SessionManager;
import com.tpago.movil.dep.domain.util.EventBus;
import com.tpago.movil.dep.ui.main.recipients.NonAffiliatedPhoneNumberRecipientAddition1Fragment;
import com.tpago.movil.dep.ui.main.recipients.NonAffiliatedPhoneNumberRecipientAddition2Fragment;
import com.tpago.movil.init.InitComponent;
import com.tpago.movil.init.InitModule;
import com.tpago.movil.main.MainComponent;
import com.tpago.movil.main.MainModule;
import com.tpago.movil.net.NetModule;
import com.tpago.movil.nfc.NfcModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * @author hecvasro
 */
@Singleton
@Component(modules = {
  AppModule.class,
  NetModule.class,
  ApiModule.class,
  NfcModule.class,
  // Deprecated modules
  DepAppModule.class,
  DepDataModule.class
})
public interface AppComponent {
  InitComponent plus(ActivityModule activityModule, InitModule initModule);
  MainComponent plus(ActivityModule activityModule, MainModule mainModule);

  // Deprecated injects
  void inject(NonAffiliatedPhoneNumberRecipientAddition1Fragment fragment);
  void inject(NonAffiliatedPhoneNumberRecipientAddition2Fragment fragment);

  // Deprecated provides
  DepAssetProvider provideResourceProvider();
  BalanceManager provideBalanceManager();
  ConfigManager provideConfigManager();
  Context provideContext();
  DepApiBridge provideApiBridge();
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
  UserStore provideUserStore();
}
