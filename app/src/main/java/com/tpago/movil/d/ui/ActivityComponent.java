package com.tpago.movil.d.ui;

import com.tpago.movil.app.ui.ActivityScope;
import com.tpago.movil.app.ui.ActivityModule;
import com.tpago.movil.dep.AppComponent;
import com.tbruyelle.rxpermissions.RxPermissions;

import dagger.Component;

/**
 * @author hecvasro
 */
@ActivityScope
@Component(
  dependencies = AppComponent.class,
  modules = {
    ActivityModule.class,
    DepActivityModule.class
  }
)
@Deprecated
public interface ActivityComponent {

  void inject(DepBaseActivity activity);

  AppDialog.Creator provideScreenDialogCreator();

  RxPermissions providePermissionManager();
}
