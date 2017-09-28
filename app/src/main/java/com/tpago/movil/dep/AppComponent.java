package com.tpago.movil.dep;

import android.content.Context;

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
import com.tpago.movil.d.ui.main.recipient.addition.NonAffiliatedPhoneNumberRecipientAddition1Fragment;
import com.tpago.movil.d.ui.main.recipient.addition.NonAffiliatedPhoneNumberRecipientAddition2Fragment;
import com.tpago.movil.d.ui.main.recipient.index.disburse.DisbursementActivity;
import com.tpago.movil.d.ui.main.transaction.own.OwnTransactionCreationActivity;
import com.tpago.movil.d.ui.main.transaction.own.OwnTransferActivity;
import com.tpago.movil.data.StringMapper;
import com.tpago.movil.d.domain.BankProvider;
import com.tpago.movil.dep.init.InitComponent;
import com.tpago.movil.dep.init.InitModule;
import com.tpago.movil.dep.main.MainComponent;
import com.tpago.movil.dep.main.MainModule;
import com.tpago.movil.dep.net.NetworkService;

import org.greenrobot.eventbus.EventBus;

/**
 * @author hecvasro
 */
@Deprecated
public interface AppComponent {

  @Deprecated
  BalanceManager provideBalanceManager();

  @Deprecated
  BankProvider provideBankProvider();

  @Deprecated
  ConfigManager provideConfigManager();

  @Deprecated
  Context provideContext();

  @Deprecated
  DepApiBridge provideApiBridge();

  @Deprecated
  EventBus eventBus();

  @Deprecated
  InitComponent plus(ActivityModule activityModule, InitModule initModule);

  @Deprecated
  InitialDataLoader provideInitialDataLoader();

  @Deprecated
  MainComponent plus(ActivityModule activityModule, MainModule mainModule);

  @Deprecated
  NetworkService provideNetworkService();

  @Deprecated
  PosBridge providePosBridge();

  @Deprecated
  ProductManager provideProductManager();

  @Deprecated
  RecipientManager provideRecipientManager();

  @Deprecated
  SchedulerProvider provideSchedulerProvider();

  @Deprecated
  SessionManager provideSessionManager();

  @Deprecated
  StringHelper provideStringHelper();

  @Deprecated
  StringMapper stringMapper();

  @Deprecated
  TransactionRepo provideTransactionRepo();

  @Deprecated
  UserStore provideUserStore();

  @Deprecated
  com.tpago.movil.d.domain.util.EventBus provideEventBus();

  @Deprecated
  void inject(App app);

  @Deprecated
  void inject(DisbursementActivity activity);

  @Deprecated
  void inject(NonAffiliatedPhoneNumberRecipientAddition1Fragment fragment);

  @Deprecated
  void inject(NonAffiliatedPhoneNumberRecipientAddition2Fragment fragment);

  @Deprecated
  void inject(OwnTransactionCreationActivity activity);

  @Deprecated
  void inject(OwnTransferActivity activity);
}
