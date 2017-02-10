package com.tpago.movil.d.ui;

import com.tpago.movil.d.AppComponent;
import com.tpago.movil.d.ui.main.PinConfirmator;
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
