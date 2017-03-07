package com.tpago.movil.app;

import com.tpago.movil.util.Preconditions;

import dagger.Module;
import dagger.Provides;

/**
 * @author hecvasro
 */
@Module
public class ActivityModule {
  private final BaseActivity activity;

  public ActivityModule(BaseActivity activity) {
    this.activity = Preconditions.checkNotNull(activity, "activity == null");
  }

  @Provides
  @ActivityScope
  BaseActivity provideActivity() {
    return activity;
  }

  @Provides
  @ActivityScope
  BackEventHandler provideBackEventHandler() {
    return new BackEventHandler();
  }
}
