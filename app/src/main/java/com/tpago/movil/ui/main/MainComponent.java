package com.tpago.movil.ui.main;

import android.content.Context;

import com.tpago.movil.AppComponent;
import com.tpago.movil.data.StringHelper;
import com.tpago.movil.data.SchedulerProvider;
import com.tpago.movil.data.res.AssetProvider;
import com.tpago.movil.domain.ProductManager;
import com.tpago.movil.domain.BalanceManager;
import com.tpago.movil.domain.api.ApiBridge;
import com.tpago.movil.domain.pos.PosBridge;
import com.tpago.movil.domain.session.SessionManager;
import com.tpago.movil.domain.util.EventBus;
import com.tpago.movil.domain.RecipientManager;
import com.tpago.movil.ui.ActivityComponent;
import com.tpago.movil.ui.ActivityModule;
import com.tpago.movil.ui.ActivityScope;

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

  ApiBridge provideApiBridge();
  AssetProvider provideResourceProvider();
  BalanceManager provideBalanceManager();
  Context provideContext();
  EventBus provideEventBus();
  PosBridge providePosBridge();
  ProductManager provideAccountManager();
  RecipientManager provideRecipientManager();
  SchedulerProvider provideSchedulerProvider();
  StringHelper provideMessageHelper();
}
