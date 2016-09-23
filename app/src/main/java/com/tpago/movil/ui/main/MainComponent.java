package com.tpago.movil.ui.main;

import com.tpago.movil.AppComponent;
import com.tpago.movil.data.MessageHelper;
import com.tpago.movil.domain.BalanceManager;
import com.tpago.movil.domain.DataLoader;
import com.tpago.movil.ui.ActivityScope;

import dagger.Component;

/**
 * TODO
 *
 * @author hecvasro
 */
@ActivityScope
@Component(modules = MainModule.class, dependencies = AppComponent.class)
public interface MainComponent {
  /**
   * TODO
   *
   * @param activity
   *   TODO
   */
  void inject(MainActivity activity);

  /**
   * TODO
   *
   * @return TODO
   */
  MessageHelper provideMessageHelper();

  /**
   * TODO
   *
   * @return TODO
   */
  DataLoader provideDataLoader();

  /**
   * TODO
   *
   * @return TODO
   */
  BalanceManager provideBalanceManager();
}
