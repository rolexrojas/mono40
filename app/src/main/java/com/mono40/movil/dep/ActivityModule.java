package com.mono40.movil.dep;

import com.mono40.movil.app.ui.activity.ActivityScope;
import com.mono40.movil.util.ObjectHelper;

import dagger.Module;
import dagger.Provides;

/**
 * @author hecvasro
 */
@Deprecated
@Module
public class ActivityModule {

  private final ActivityBase activity;

  public ActivityModule(ActivityBase activity) {
    this.activity = ObjectHelper.checkNotNull(activity, "activity");
  }

  @Provides
  @ActivityScope
  ActivityBase provideActivity() {
    return activity;
  }

  @Provides
  @ActivityScope
  BackEventHandler provideBackEventHandler() {
    return new BackEventHandler();
  }
}
