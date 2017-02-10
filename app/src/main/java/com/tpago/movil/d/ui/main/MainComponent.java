package com.tpago.movil.d.ui.main;

import android.content.Context;

import com.tpago.movil.d.AppComponent;
import com.tpago.movil.d.data.StringHelper;
import com.tpago.movil.d.data.SchedulerProvider;
import com.tpago.movil.d.data.res.AssetProvider;
import com.tpago.movil.d.domain.ProductManager;
import com.tpago.movil.d.domain.BalanceManager;
import com.tpago.movil.d.domain.api.ApiBridge;
import com.tpago.movil.d.domain.pos.PosBridge;
import com.tpago.movil.d.domain.session.SessionManager;
import com.tpago.movil.d.domain.util.EventBus;
import com.tpago.movil.d.domain.RecipientManager;
import com.tpago.movil.d.ui.ActivityComponent;
import com.tpago.movil.d.ui.ActivityModule;
import com.tpago.movil.d.ui.ActivityScope;

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
