package com.tpago.movil.app.ui.activity.toolbar;

import com.tpago.movil.app.di.ComponentBuilder;
import com.tpago.movil.app.ui.activity.ActivityScope;
import com.tpago.movil.app.ui.activity.base.ActivityModule;
import com.tpago.movil.app.ui.activity.fragment.ActivityModuleFragment;

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
