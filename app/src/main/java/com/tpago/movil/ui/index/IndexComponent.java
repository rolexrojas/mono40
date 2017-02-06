package com.tpago.movil.ui.index;

import com.tpago.movil.AppComponent;
import com.tpago.movil.ui.ActivityModule;
import com.tpago.movil.ui.ActivityScope;

import dagger.Component;

/**
 * TODO
 *
 * @author hecvasro
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = { ActivityModule.class,
  IndexModule.class })
interface IndexComponent {
  /**
   * TODO
   *
   * @param activity
   *   TODO
   */
  void inject(IndexActivity activity);
}
