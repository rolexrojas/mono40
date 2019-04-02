package com.tpago.movil.d.ui.main;

import android.content.Context;

import com.tpago.movil.api.Api;
import com.tpago.movil.app.ui.alert.AlertManager;
import com.tpago.movil.app.ui.fragment.base.BaseFragmentModule;
import com.tpago.movil.app.ui.main.settings.help.FragmentHelp;
import com.tpago.movil.company.CompanyHelper;
import com.tpago.movil.company.partner.PartnerStore;
import com.tpago.movil.d.ui.DepActivityBase;
import com.tpago.movil.dep.User;
import com.tpago.movil.app.ui.main.settings.profile.ProfileComponent;
import com.tpago.movil.app.ui.main.settings.profile.ProfileModule;
import com.tpago.movil.d.data.StringHelper;
import com.tpago.movil.d.data.SchedulerProvider;
import com.tpago.movil.d.domain.ProductManager;
import com.tpago.movil.d.domain.BalanceManager;
import com.tpago.movil.d.domain.api.DepApiBridge;
import com.tpago.movil.d.domain.pos.PosBridge;
import com.tpago.movil.d.domain.util.EventBus;
import com.tpago.movil.d.domain.RecipientManager;
import com.tpago.movil.d.ui.ActivityComponent;
import com.tpago.movil.app.StringMapper;
import com.tpago.movil.dep.net.NetworkService;
import com.tpago.movil.paypal.PayPalAccountStore;
import com.tpago.movil.session.SessionManager;

public interface DepMainComponent extends ActivityComponent {

  void inject(DepMainActivityBase activity);

  void inject(FragmentHelp fragment);

  CompanyHelper logoFactory();

  StringMapper stringMapper();

  AlertManager alertManager();

  void inject(DepActivityBase activity);

  SessionManager sessionManager();

  ProfileComponent create(BaseFragmentModule baseFragmentModule, ProfileModule profileModule);

  BalanceManager provideBalanceManager();

  Context provideContext();

  DepApiBridge provideApiBridge();

  EventBus provideEventBus();

  NetworkService provideNetworkService();

  PosBridge providePosBridge();

  ProductManager provideAccountManager();

  RecipientManager provideRecipientManager();

  SchedulerProvider provideSchedulerProvider();

  StringHelper provideMessageHelper();

  User provideUser();

  PayPalAccountStore payPalAccountStore();

  PartnerStore partnerStore();

  Api api();
}
