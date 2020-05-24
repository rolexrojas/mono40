package com.mono40.movil.d.ui.main;

import android.content.Context;

import com.mono40.movil.api.Api;
import com.mono40.movil.app.ui.alert.AlertManager;
import com.mono40.movil.app.ui.fragment.base.BaseFragmentModule;
import com.mono40.movil.app.ui.main.settings.help.FragmentHelp;
import com.mono40.movil.company.CompanyHelper;
import com.mono40.movil.company.partner.PartnerStore;
import com.mono40.movil.d.ui.DepActivityBase;
import com.mono40.movil.dep.User;
import com.mono40.movil.app.ui.main.settings.profile.ProfileComponent;
import com.mono40.movil.app.ui.main.settings.profile.ProfileModule;
import com.mono40.movil.d.data.StringHelper;
import com.mono40.movil.d.data.SchedulerProvider;
import com.mono40.movil.d.domain.ProductManager;
import com.mono40.movil.d.domain.BalanceManager;
import com.mono40.movil.d.domain.api.DepApiBridge;
import com.mono40.movil.d.domain.pos.PosBridge;
import com.mono40.movil.d.domain.util.EventBus;
import com.mono40.movil.d.domain.RecipientManager;
import com.mono40.movil.d.ui.ActivityComponent;
import com.mono40.movil.app.StringMapper;
import com.mono40.movil.dep.net.NetworkService;
import com.mono40.movil.paypal.PayPalAccountStore;
import com.mono40.movil.session.SessionManager;

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
