package com.mono40.movil.dep;

import android.content.Context;

import com.mono40.movil.api.Api;
import com.mono40.movil.app.ui.init.unlock.ChangePassword.ChangePasswordFragment;
import com.mono40.movil.company.CompanyHelper;
import com.mono40.movil.company.bank.BankStore;
import com.mono40.movil.company.partner.PartnerStore;
import com.mono40.movil.d.data.SchedulerProvider;
import com.mono40.movil.d.data.StringHelper;
import com.mono40.movil.d.domain.BalanceManager;
import com.mono40.movil.d.domain.InitialDataLoader;
import com.mono40.movil.d.domain.ProductManager;
import com.mono40.movil.d.domain.RecipientManager;
import com.mono40.movil.d.domain.api.DepApiBridge;
import com.mono40.movil.d.domain.pos.PosBridge;
import com.mono40.movil.d.ui.main.recipient.addition.NonAffiliatedBanreservasPhoneNumberRecipientAdditionFragment;
import com.mono40.movil.d.ui.main.recipient.addition.NonAffiliatedPhoneNumberRecipientAddition1Fragment;
import com.mono40.movil.d.ui.main.recipient.addition.NonAffiliatedPhoneNumberRecipientAddition2Fragment;
import com.mono40.movil.d.ui.main.recipient.index.disburse.DisbursementActivity;
import com.mono40.movil.d.ui.main.transaction.own.OwnTransactionCreationActivity;
import com.mono40.movil.d.ui.main.transaction.own.OwnTransferActivity;
import com.mono40.movil.app.StringMapper;
import com.mono40.movil.dep.init.InitComponent;
import com.mono40.movil.dep.init.InitModule;
import com.mono40.movil.dep.main.transactions.PaymentMethodChooser;
import com.mono40.movil.dep.net.NetworkService;
import com.mono40.movil.session.SessionManager;

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
    com.mono40.movil.app.ui.activity.base.ActivityModule activityModule,
    com.mono40.movil.dep.ActivityModule depActivityModule,
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
  com.mono40.movil.d.domain.util.EventBus provideEventBus();

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
