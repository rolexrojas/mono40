package com.gbh.movil.ui;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.tbruyelle.rxpermissions.RxPermissions;

import dagger.Module;
import dagger.Provides;

/**
 * @author hecvasro
 */
@Module
public class ActivityModule {
  private final Activity activity;

  public ActivityModule(@NonNull Activity activity) {
    this.activity = activity;
  }

  @Provides
  @ActivityScope
  ScreenDialog.Creator provideScreenDialogCreator() {
    return new ScreenDialog.Creator(activity);
  }

  @Provides
  @ActivityScope
  RxPermissions providePermissionManager() {
    return new RxPermissions(activity);
  }
}
