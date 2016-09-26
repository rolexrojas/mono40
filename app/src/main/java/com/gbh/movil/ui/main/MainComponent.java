package com.gbh.movil.ui.main;

import com.gbh.movil.AppComponent;
import com.gbh.movil.data.MessageHelper;
import com.gbh.movil.domain.BalanceManager;
import com.gbh.movil.domain.DataLoader;
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
