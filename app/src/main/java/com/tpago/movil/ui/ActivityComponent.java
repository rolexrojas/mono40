package com.tpago.movil.ui;

import com.tpago.movil.AppComponent;
import com.tpago.movil.ui.main.PinConfirmator;
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
