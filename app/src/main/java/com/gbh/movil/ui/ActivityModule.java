package com.gbh.movil.ui;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.gbh.movil.ui.main.PinConfirmator;
import com.tbruyelle.rxpermissions.RxPermissions;

import dagger.Module;
import dagger.Provides;

/**
 * @author hecvasro
 */
@Module
public class ActivityModule {
  private final AppCompatActivity activity;

  public ActivityModule(@NonNull AppCompatActivity activity) {
    this.activity = activity;
  }

  @Provides
  @ActivityScope
  AppDialog.Creator provideScreenDialogCreator() {
    return new AppDialog.Creator(activity);
  }

  @Provides
  @ActivityScope
  RxPermissions providePermissionManager() {
    return new RxPermissions(activity);
  }

  @Provides
  @ActivityScope
  PinConfirmator providePinConfirmator() {
    return new PinConfirmator(activity.getSupportFragmentManager());
  }
}
