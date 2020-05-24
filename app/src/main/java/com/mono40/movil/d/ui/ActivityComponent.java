package com.mono40.movil.d.ui;

import com.tbruyelle.rxpermissions.RxPermissions;

/**
 * @author hecvasro
 */
public interface ActivityComponent {

  void inject(DepActivityBase activity);

  AppDialog.Creator provideScreenDialogCreator();

  RxPermissions providePermissionManager();
}
