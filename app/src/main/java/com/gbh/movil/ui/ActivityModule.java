package com.gbh.movil.ui;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.gbh.movil.ui.main.PinConfirmator;
import com.gbh.movil.ui.view.widget.FullScreenLoadIndicator;
import com.gbh.movil.ui.view.widget.LoadIndicator;
import com.tbruyelle.rxpermissions.RxPermissions;

import dagger.Module;
import dagger.Provides;

/**
 * TODO
 *
 * @author hecvasro
 */
@Module
public class ActivityModule {
  private final AppCompatActivity activity;

  /**
   * TODO
   *
   * @param activity
   *   TODO
   */
  public ActivityModule(@NonNull AppCompatActivity activity) {
    this.activity = activity;
  }

  /**
   * TODO
   *
   * @return TODO
   */
  @Provides
  @ActivityScope
  MessageDispatcher provideMessageShower() {
    return new ToastMessageDispatcher(activity);
  }

  /**
   * TODO
   *
   * @return TODO
   */
  @Provides
  @ActivityScope
  LoadIndicator provideLoadIndicator() {
    return new FullScreenLoadIndicator(activity.getSupportFragmentManager());
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
