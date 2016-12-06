package com.gbh.movil.ui;

import com.gbh.movil.AppComponent;
import com.tbruyelle.rxpermissions.RxPermissions;

import dagger.Component;

/**
 * @author hecvasro
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {
  void inject(BaseActivity activity);

  ScreenDialog.Creator provideScreenDialogCreator();

  RxPermissions providePermissionManager();
}
