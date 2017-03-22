package com.tpago.movil.d.ui;

import com.tpago.movil.app.ActivityScope;
import com.tpago.movil.app.AppComponent;
import com.tbruyelle.rxpermissions.RxPermissions;

import dagger.Component;

/**
 * @author hecvasro
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = DepActivityModule.class)
@Deprecated
public interface ActivityComponent {
  void inject(BaseActivity activity);

  AppDialog.Creator provideScreenDialogCreator();
  RxPermissions providePermissionManager();
}
