package com.tpago.movil.dep.ui.main;

import android.content.Context;

import com.tpago.movil.User;
import com.tpago.movil.UserStore;
import com.tpago.movil.app.ActivityScope;
import com.tpago.movil.app.AppComponent;
import com.tpago.movil.dep.data.StringHelper;
import com.tpago.movil.dep.data.SchedulerProvider;
import com.tpago.movil.dep.domain.ProductManager;
import com.tpago.movil.dep.domain.BalanceManager;
import com.tpago.movil.dep.domain.api.DepApiBridge;
import com.tpago.movil.dep.domain.pos.PosBridge;
import com.tpago.movil.dep.domain.session.SessionManager;
import com.tpago.movil.dep.domain.util.EventBus;
import com.tpago.movil.dep.domain.RecipientManager;
import com.tpago.movil.dep.ui.ActivityComponent;
import com.tpago.movil.dep.ui.DepActivityModule;
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
