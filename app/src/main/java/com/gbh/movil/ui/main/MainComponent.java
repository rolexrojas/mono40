package com.gbh.movil.ui.main;

import com.gbh.movil.AppComponent;
import com.gbh.movil.data.MessageHelper;
import com.gbh.movil.domain.AccountManager;
import com.gbh.movil.domain.BalanceManager;
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

  MessageHelper provideMessageHelper();

  AccountManager provideAccountManager();

  BalanceManager provideBalanceManager();
}
