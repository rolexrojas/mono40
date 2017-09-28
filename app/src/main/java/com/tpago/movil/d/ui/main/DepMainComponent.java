package com.tpago.movil.d.ui.main;

import android.content.Context;

import com.tpago.movil.dep.Session;
import com.tpago.movil.dep.User;
import com.tpago.movil.dep.UserStore;
import com.tpago.movil.app.ui.ActivityScope;
import com.tpago.movil.dep.AppComponent;
import com.tpago.movil.app.ui.FragmentActivityModule;
import com.tpago.movil.app.ui.main.settings.SettingsComponent;
import com.tpago.movil.app.ui.main.settings.SettingsModule;
import com.tpago.movil.app.ui.main.profile.ProfileComponent;
import com.tpago.movil.app.ui.main.profile.ProfileModule;
import com.tpago.movil.d.data.StringHelper;
import com.tpago.movil.d.data.SchedulerProvider;
import com.tpago.movil.d.domain.ProductManager;
import com.tpago.movil.d.domain.BalanceManager;
import com.tpago.movil.d.domain.api.DepApiBridge;
import com.tpago.movil.d.domain.pos.PosBridge;
import com.tpago.movil.d.domain.session.SessionManager;
import com.tpago.movil.d.domain.util.EventBus;
import com.tpago.movil.d.domain.RecipientManager;
import com.tpago.movil.d.ui.ActivityComponent;
import com.tpago.movil.d.ui.DepActivityModule;
import com.tpago.movil.d.ui.main.recipient.index.disburse.DisbursementFragment;
import com.tpago.movil.data.StringMapper;
import com.tpago.movil.dep.main.MainModule;
import com.tpago.movil.dep.net.NetworkService;

import dagger.Component;

/**
 * @author hecvasro
 */
@Deprecated
@ActivityScope
@Component(
  dependencies = AppComponent.class,
  modules = {
    FragmentActivityModule.class,
    DepActivityModule.class,
    MainModule.class,
    DepMainModule.class
  })
public interface DepMainComponent extends ActivityComponent {

  void inject(DepMainActivity activity);

  void inject(DisbursementFragment fragment);

  StringMapper stringMapper();

  SettingsComponent create(SettingsModule module);

  ProfileComponent create(ProfileModule module);

  BalanceManager provideBalanceManager();

  Context provideContext();

  DepApiBridge provideApiBridge();

  EventBus provideEventBus();

  NetworkService provideNetworkService();

  PosBridge providePosBridge();

  ProductManager provideAccountManager();

  RecipientManager provideRecipientManager();

  SchedulerProvider provideSchedulerProvider();

  Session provideSession();

  SessionManager provideSessionManager();

  StringHelper provideMessageHelper();

  User provideUser();

  UserStore provideUserStore();
}
