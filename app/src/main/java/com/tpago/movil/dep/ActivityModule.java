package com.tpago.movil.dep;

import com.tpago.movil.app.ui.ActivityScope;

import dagger.Module;
import dagger.Provides;

/**
 * @author hecvasro
 */
@Deprecated
@Module
public class ActivityModule {

  private final BaseActivity activity;

  public ActivityModule(BaseActivity activity) {
    this.activity = Preconditions.assertNotNull(activity, "activity == null");
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
