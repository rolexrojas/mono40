package com.tpago.movil.d.ui.index;

import com.tpago.movil.d.AppComponent;
import com.tpago.movil.d.ui.ActivityModule;
import com.tpago.movil.d.ui.ActivityScope;

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
