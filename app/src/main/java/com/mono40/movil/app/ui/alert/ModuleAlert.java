package com.mono40.movil.app.ui.alert;

import com.mono40.movil.app.ui.activity.ActivityScope;
import com.mono40.movil.app.ui.activity.base.ActivityBase;
import com.mono40.movil.app.StringMapper;

import dagger.Module;
import dagger.Provides;

/**
 * @author hecvasro
 */
@Module
public final class ModuleAlert {

  @Provides
  @ActivityScope
  AlertManager alertManager(ActivityBase activity, StringMapper stringMapper) {
    return AlertManager.create(activity, stringMapper);
  }
}
