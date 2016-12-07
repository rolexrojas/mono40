package com.gbh.movil.ui;

import com.gbh.movil.AppComponent;
import com.gbh.movil.ui.main.PinConfirmator;
import com.tbruyelle.rxpermissions.RxPermissions;

import dagger.Component;

/**
 * @author hecvasro
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {
  void inject(BaseActivity activity);

  AppDialog.Creator provideScreenDialogCreator();

  RxPermissions providePermissionManager();

  PinConfirmator providePinConfirmator();
}
