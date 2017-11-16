package com.tpago.movil.dep;

import com.tpago.movil.app.ui.ActivityScope;
import com.tpago.movil.util.ObjectHelper;

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
    this.activity = ObjectHelper.checkNotNull(activity, "activity");
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
