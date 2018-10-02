package com.tpago.movil.d.ui;

import com.tbruyelle.rxpermissions.RxPermissions;

/**
 * @author hecvasro
 */
public interface ActivityComponent {

  void inject(DepActivityBase activity);

  AppDialog.Creator provideScreenDialogCreator();

  RxPermissions providePermissionManager();
}
