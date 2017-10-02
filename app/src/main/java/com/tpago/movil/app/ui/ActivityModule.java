package com.tpago.movil.app.ui;

import android.support.v4.app.FragmentManager;

import com.tpago.movil.util.ObjectHelper;

import dagger.Module;
import dagger.Provides;

/**
 * @author hecvasro
 */
@Module
public final class ActivityModule {

  public static ActivityModule create(BaseActivity activity) {
    return new ActivityModule(activity);
  }

  private final BaseActivity activity;

  private ActivityModule(BaseActivity activity) {
    this.activity = ObjectHelper.checkNotNull(activity, "activity");
  }

  @Provides
  @ActivityScope
  FragmentManager fragmentManager() {
    return this.activity.getSupportFragmentManager();
  }

  @Provides
  @ActivityScope
  @BackButton
  NavButtonClickHandler navButtonClickHandler() {
    return NavButtonClickHandler.create();
  }

  @Provides
  @ActivityScope
  AlertManager alertManager() {
    return AlertManager.create(this.activity);
  }

  @Provides
  @ActivityScope
  TakeoverLoader takeoverLoader(FragmentManager fragmentManager) {
    return TakeoverLoader.create(fragmentManager);
  }
}
