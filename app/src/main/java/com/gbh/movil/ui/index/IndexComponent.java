package com.gbh.movil.ui.index;

import com.gbh.movil.AppComponent;
import com.gbh.movil.ui.ActivityModule;
import com.gbh.movil.ui.ActivityScope;

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
