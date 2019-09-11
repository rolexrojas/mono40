package com.tpago.movil.d.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.tpago.movil.app.ui.activity.ActivityScope;
import com.tpago.movil.d.ui.view.widget.FullScreenLoadIndicator;
import com.tpago.movil.d.ui.view.widget.LoadIndicator;
import com.tbruyelle.rxpermissions.RxPermissions;

import dagger.Module;
import dagger.Provides;

/**
 * TODO
 *
 * @author hecvasro
 */
@Module
@Deprecated
public class DepActivityModule {

  private final AppCompatActivity activity;

  /**
   * TODO
   *
   * @param activity
   *   TODO
   */
  public DepActivityModule(@NonNull AppCompatActivity activity) {
    this.activity = activity;
  }

  /**
   * TODO
   *
   * @return TODO
   */
  @Provides
  @ActivityScope
  MessageDispatcher provideMessageDispatcher() {
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
}
