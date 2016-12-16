package com.gbh.movil.ui.main;

import android.content.Context;

import com.gbh.movil.AppComponent;
import com.gbh.movil.data.StringHelper;
import com.gbh.movil.data.SchedulerProvider;
import com.gbh.movil.data.res.AssetProvider;
import com.gbh.movil.domain.ProductManager;
import com.gbh.movil.domain.BalanceManager;
import com.gbh.movil.domain.api.ApiBridge;
import com.gbh.movil.domain.pos.PosBridge;
import com.gbh.movil.domain.session.Session;
import com.gbh.movil.domain.session.SessionManager;
import com.gbh.movil.domain.util.EventBus;
import com.gbh.movil.domain.RecipientManager;
import com.gbh.movil.ui.ActivityComponent;
import com.gbh.movil.ui.ActivityModule;
import com.gbh.movil.ui.ActivityScope;

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
