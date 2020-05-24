package com.mono40.movil.app.ui.activity.toolbar;

import com.mono40.movil.app.di.ComponentBuilder;
import com.mono40.movil.app.ui.activity.ActivityScope;
import com.mono40.movil.app.ui.activity.base.ActivityModule;
import com.mono40.movil.app.ui.activity.fragment.ActivityModuleFragment;

import dagger.Subcomponent;

/**
 * @author hecvasro
 */
@ActivityScope
@Subcomponent(modules = {
  ActivityModule.class,
  ActivityModuleFragment.class,
  ActivityModuleToolbar.class,
  ActivityModuleComponentBuilderToolbar.class
})
public interface ActivityComponentToolbar {

  void inject(ActivityToolbar activity);

  @Subcomponent.Builder
  interface Builder extends ComponentBuilder<ActivityComponentToolbar> {

    Builder activityModule(ActivityModule module);

    Builder activityModuleToolbar(ActivityModuleToolbar module);
  }
}
