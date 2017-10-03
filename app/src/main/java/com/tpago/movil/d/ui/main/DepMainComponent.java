package com.tpago.movil.d.ui.main;

import android.content.Context;

import com.tpago.movil.dep.Session;
import com.tpago.movil.dep.User;
import com.tpago.movil.dep.UserStore;
import com.tpago.movil.app.ui.main.settings.profile.ProfileComponent;
import com.tpago.movil.app.ui.main.settings.profile.ProfileModule;
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
import com.tpago.movil.d.ui.main.recipient.index.disburse.DisbursementFragment;
import com.tpago.movil.data.StringMapper;
import com.tpago.movil.dep.net.NetworkService;
import com.tpago.movil.domain.auth.alt.AltAuthMethodManager;

/**
 * @author hecvasro
 */
public interface DepMainComponent extends ActivityComponent {

  void inject(DepMainActivity activity);

  void inject(DisbursementFragment fragment);

  StringMapper stringMapper();

  AltAuthMethodManager altAuthManager();

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
