package com.gbh.movil.ui.main;

import com.gbh.movil.AppComponent;
import com.gbh.movil.data.StringHelper;
import com.gbh.movil.data.SchedulerProvider;
import com.gbh.movil.data.res.ResourceProvider;
import com.gbh.movil.domain.ProductManager;
import com.gbh.movil.domain.BalanceManager;
import com.gbh.movil.domain.util.EventBus;
import com.gbh.movil.domain.RecipientManager;
import com.gbh.movil.ui.ActivityScope;

import dagger.Component;

/**
 * TODO
 *
 * @author hecvasro
 */
@ActivityScope
@Component(modules = MainModule.class, dependencies = AppComponent.class)
public interface MainComponent {
  void inject(MainActivity activity);

  StringHelper provideMessageHelper();

  SchedulerProvider provideSchedulerProvider();

  ResourceProvider provideResourceProvider();

  EventBus provideEventBus();

  ProductManager provideAccountManager();

  BalanceManager provideBalanceManager();

  RecipientManager provideRecipientManager();
}
