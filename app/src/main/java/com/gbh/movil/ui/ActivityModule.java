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

  /**
   * TODO
   *
   * @param activity
   *   TODO
   */
  public ActivityModule(@NonNull Activity activity) {
    this.activity = activity;
  }

  /**
   * TODO
   *
   * @return TODO
   */
  @Provides
  @ActivityScope
  RxPermissions providePermissionManager() {
    return new RxPermissions(activity);
  }
}
