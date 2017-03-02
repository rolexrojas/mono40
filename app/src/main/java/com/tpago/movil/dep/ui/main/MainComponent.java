package com.tpago.movil.dep.ui.main;

import android.content.Context;

import com.tpago.movil.UserStore;
import com.tpago.movil.app.AppComponent;
import com.tpago.movil.dep.data.StringHelper;
import com.tpago.movil.dep.data.SchedulerProvider;
import com.tpago.movil.dep.data.res.AssetProvider;
import com.tpago.movil.dep.domain.ProductManager;
import com.tpago.movil.dep.domain.BalanceManager;
import com.tpago.movil.dep.domain.api.DepApiBridge;
import com.tpago.movil.dep.domain.pos.PosBridge;
import com.tpago.movil.dep.domain.session.SessionManager;
import com.tpago.movil.dep.domain.util.EventBus;
import com.tpago.movil.dep.domain.RecipientManager;
import com.tpago.movil.dep.ui.ActivityComponent;
import com.tpago.movil.dep.ui.ActivityModule;
import com.tpago.movil.dep.ui.ActivityScope;

import dagger.Component;

/**
 * TODO
 *
 * @author hecvasro
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = { ActivityModule.class, MainModule.class })
public interface MainComponent extends ActivityComponent {
  void inject(MainActivity activity);

  SessionManager provideSessionManager();

  DepApiBridge provideApiBridge();
  AssetProvider provideResourceProvider();
  BalanceManager provideBalanceManager();
  Context provideContext();
  EventBus provideEventBus();
  PosBridge providePosBridge();
  ProductManager provideAccountManager();
  RecipientManager provideRecipientManager();
  SchedulerProvider provideSchedulerProvider();
  StringHelper provideMessageHelper();
  UserStore provideUserStore();
}
