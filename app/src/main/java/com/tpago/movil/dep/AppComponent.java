package com.tpago.movil.dep;

import android.content.Context;

import com.tpago.movil.api.Api;
import com.tpago.movil.app.ui.init.unlock.ChangePassword.ChangePasswordFragment;
import com.tpago.movil.company.CompanyHelper;
import com.tpago.movil.company.bank.BankStore;
import com.tpago.movil.company.partner.PartnerStore;
import com.tpago.movil.d.data.SchedulerProvider;
import com.tpago.movil.d.data.StringHelper;
import com.tpago.movil.d.domain.BalanceManager;
import com.tpago.movil.d.domain.InitialDataLoader;
import com.tpago.movil.d.domain.ProductManager;
import com.tpago.movil.d.domain.RecipientManager;
import com.tpago.movil.d.domain.api.DepApiBridge;
import com.tpago.movil.d.domain.pos.PosBridge;
import com.tpago.movil.d.ui.main.recipient.addition.NonAffiliatedBanreservasPhoneNumberRecipientAdditionFragment;
import com.tpago.movil.d.ui.main.recipient.addition.NonAffiliatedPhoneNumberRecipientAddition1Fragment;
import com.tpago.movil.d.ui.main.recipient.addition.NonAffiliatedPhoneNumberRecipientAddition2Fragment;
import com.tpago.movil.d.ui.main.recipient.index.disburse.DisbursementActivity;
import com.tpago.movil.d.ui.main.transaction.own.OwnTransactionCreationActivity;
import com.tpago.movil.d.ui.main.transaction.own.OwnTransferActivity;
import com.tpago.movil.app.StringMapper;
import com.tpago.movil.dep.init.InitComponent;
import com.tpago.movil.dep.init.InitModule;
import com.tpago.movil.dep.main.transactions.PaymentMethodChooser;
import com.tpago.movil.dep.net.NetworkService;
import com.tpago.movil.session.SessionManager;

/**
 * @author hecvasro
 */
@Deprecated
public interface AppComponent {

  Api api();

  BankStore bankStore();

  CompanyHelper logoFactory();

  PartnerStore partnerStore();

  SessionManager sessionManager();

  @Deprecated
  BalanceManager provideBalanceManager();

  @Deprecated
  ConfigManager provideConfigManager();

  @Deprecated
  Context provideContext();

  @Deprecated
  DepApiBridge provideApiBridge();

  @Deprecated
  InitComponent plus(
    com.tpago.movil.app.ui.activity.base.ActivityModule activityModule,
    com.tpago.movil.dep.ActivityModule depActivityModule,
    InitModule initModule
  );

  @Deprecated
  InitialDataLoader provideInitialDataLoader();

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
  StringHelper provideStringHelper();

  @Deprecated
  StringMapper stringMapper();

  @Deprecated
  com.tpago.movil.d.domain.util.EventBus provideEventBus();

  @Deprecated
  void inject(App app);

  @Deprecated
  void inject(DisbursementActivity activity);

  @Deprecated
  void inject(NonAffiliatedPhoneNumberRecipientAddition1Fragment fragment);

  @Deprecated
  void inject(NonAffiliatedBanreservasPhoneNumberRecipientAdditionFragment fragment);

  @Deprecated
  void inject(NonAffiliatedPhoneNumberRecipientAddition2Fragment fragment);

  @Deprecated
  void inject(OwnTransactionCreationActivity activity);

  @Deprecated
  void inject(OwnTransferActivity activity);

  @Deprecated
  void inject(PaymentMethodChooser paymentMethodChooser);

  @Deprecated
  void inject(ChangePasswordFragment fragment);
}
