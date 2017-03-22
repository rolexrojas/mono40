package com.tpago.movil.d.ui.main;

import android.content.Context;

import com.tpago.movil.User;
import com.tpago.movil.UserStore;
import com.tpago.movil.app.ActivityScope;
import com.tpago.movil.app.AppComponent;
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
import com.tpago.movil.main.MainModule;

import dagger.Component;

/**
 * @author hecvasro
 */
@Deprecated
@ActivityScope
@Component(
  dependencies = AppComponent.class,
  modules = {
    DepActivityModule.class,
    MainModule.class,
    DepMainModule.class
  })
public interface DepMainComponent extends ActivityComponent {
  void inject(DepMainActivity activity);

  BalanceManager provideBalanceManager();
  Context provideContext();
  DepApiBridge provideApiBridge();
  EventBus provideEventBus();
  PosBridge providePosBridge();
  ProductManager provideAccountManager();
  RecipientManager provideRecipientManager();
  SchedulerProvider provideSchedulerProvider();
  SessionManager provideSessionManager();
  StringHelper provideMessageHelper();
  User provideUser();
  UserStore provideUserStore();
}
