package com.gbh.movil.ui;

import com.gbh.movil.AppComponent;
import com.tbruyelle.rxpermissions.RxPermissions;

import dagger.Component;

/**
 * TODO
 *
 * @author hecvasro
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {
  /**
   * TODO
   *
   * @param activity
   *   TODO
   */
  void inject(BaseActivity activity);

  /**
   * TODO
   *
   * @return TODO
   */
  RxPermissions providePermissionManager();
}
