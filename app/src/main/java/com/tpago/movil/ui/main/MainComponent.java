package com.tpago.movil.ui.main;

import com.tpago.movil.AppComponent;
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
}
