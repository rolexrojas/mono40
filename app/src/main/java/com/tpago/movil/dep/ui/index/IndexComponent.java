package com.tpago.movil.dep.ui.index;

import com.tpago.movil.app.AppComponent;
import com.tpago.movil.dep.ui.ActivityModule;
import com.tpago.movil.dep.ui.ActivityScope;

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
