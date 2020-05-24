package com.mono40.movil.app.ui.activity.base;

import androidx.fragment.app.FragmentManager;

import com.mono40.movil.app.di.ComponentBuilder;
import com.mono40.movil.app.di.ComponentBuilderSupplier;
import com.mono40.movil.app.ui.activity.NavButtonPressEventHandler;
import com.mono40.movil.app.ui.activity.ActivityQualifier;
import com.mono40.movil.app.ui.activity.ActivityScope;
import com.mono40.movil.app.ui.alert.ModuleAlert;
import com.mono40.movil.app.ui.loader.takeover.ModuleLoaderTakeover;
import com.mono40.movil.util.ObjectHelper;

import java.util.Map;

import dagger.Module;
import dagger.Provides;

/**
 * @author hecvasro
 */
@Module(includes = {
  ModuleAlert.class,
  ModuleLoaderTakeover.class
})
public final class ActivityModule {

  public static ActivityModule create(ActivityBase activity) {
    return new ActivityModule(activity);
  }

  private final ActivityBase activity;

  private ActivityModule(ActivityBase activity) {
    this.activity = ObjectHelper.checkNotNull(activity, "activity");
  }

  @Provides
  @ActivityScope
  ActivityBase activity() {
    return this.activity;
  }

  @Provides
  @ActivityScope
  FragmentManager fragmentManager(ActivityBase activity) {
    return activity.getSupportFragmentManager();
  }

  @Provides
  @ActivityScope
  NavButtonPressEventHandler backButtonPressEventHandler() {
    return NavButtonPressEventHandler.create();
  }

  @Provides
  @ActivityScope
  @ActivityQualifier
  ComponentBuilderSupplier componentBuilderSupplier(@ActivityQualifier Map<Class<?>, ComponentBuilder> map) {
    return ComponentBuilderSupplier.create(map);
  }
}
